package com.maxchauo.springserver.dao.user;

import com.maxchauo.springserver.dto.user.FbSignInDto;
import com.maxchauo.springserver.dto.user.SignInDto;
import com.maxchauo.springserver.dto.user.SignUpDto;
import com.maxchauo.springserver.exception.DatabaseException;
import com.maxchauo.springserver.model.user.User;
import com.maxchauo.springserver.rowmapper.UserRowMapper;
import com.maxchauo.springserver.util.PasswordUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao {


    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    @Autowired
    private NamedParameterJdbcTemplate template;
    @Autowired
    private PasswordUtil passwordUtil;


    @Override
    public User findByEmail(SignUpDto signUpDto) {
        String email = signUpDto.getEmail();
        String sql = "SELECT * FROM user WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        try {
            User user = template.queryForObject(sql, params, new UserRowMapper());
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public User createUser(SignUpDto signUpDto) {
        String sql = "INSERT INTO user (name, email, password, provider, picture) VALUES (:name, :email, :password, :provider, :picture)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", signUpDto.getName());
        params.addValue("email", signUpDto.getEmail());
        params.addValue("password", signUpDto.getPassword());
        params.addValue("provider", "native");
        params.addValue("picture", "picture");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, params, keyHolder, new String[]{"id"});
        Number generatedId = keyHolder.getKey();
        User user = new User();
        user.setId(generatedId.longValue());
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setProvider("native");
        user.setPicture("picture");
        return user;
    }

    @Override
    public User checkSignIn(SignInDto signInDto) {
        String email = signInDto.getEmail();
        String provider = signInDto.getProvider();
        String password = signInDto.getPassword();
        String sql = "SELECT * FROM user WHERE email = :email AND provider = :provider";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        params.addValue("provider", provider);

        try {
            User user = template.queryForObject(sql, params, new UserRowMapper());
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            } else {
                return null;
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error accessing the database", e);
        }
    }

    @Override
    public User checkFacebookEmail(FbSignInDto fbSignInDto) {
        String sql = "SELECT * FROM user WHERE email = :email ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", fbSignInDto.getEmail());
        try {
            return template.queryForObject(sql, params, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error accessing the database", e);
        }
    }

    @Override
    public User faceBookCreateUser(FbSignInDto fbSignInDto) {
        String sql = "INSERT INTO user (name,email,provider,picture) VALUES (:name,:email,:provider,:picture)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", fbSignInDto.getName());
        params.addValue("email", fbSignInDto.getEmail());
        params.addValue("provider", "facebook");
        params.addValue("picture", fbSignInDto.getPicture().getData().getUrl());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            template.update(sql, params, keyHolder, new String[]{"id"});
            Long userId = keyHolder.getKey().longValue();
            User user = new User();
            user.setId(userId);
            user.setName(fbSignInDto.getName());
            user.setEmail(fbSignInDto.getEmail());
            user.setProvider("facebook");
            user.setPicture(fbSignInDto.getPicture().getData().getUrl());
            return user;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new DatabaseException("Error accessing the database", e);
        }
    }
}


