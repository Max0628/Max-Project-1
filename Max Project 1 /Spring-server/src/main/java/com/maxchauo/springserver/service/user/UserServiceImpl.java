package com.maxchauo.springserver.service.user;


import com.maxchauo.springserver.dao.user.UserDao;
import com.maxchauo.springserver.dto.user.Data;
import com.maxchauo.springserver.dto.user.ProfileResponse;
import com.maxchauo.springserver.dto.user.SignInDto;
import com.maxchauo.springserver.dto.user.SignUpDto;
import com.maxchauo.springserver.exception.BadRequestException;
import com.maxchauo.springserver.exception.ForbiddenException;
import com.maxchauo.springserver.exception.UnauthorizedException;
import com.maxchauo.springserver.model.user.User;
import com.maxchauo.springserver.util.JWTutils;
import com.maxchauo.springserver.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordUtil passwordUtil;
    private final JWTutils jwtUtils;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordUtil passwordUtil, JWTutils jwtUtils) {
        this.userDao = userDao;
        this.passwordUtil = passwordUtil;
        this.jwtUtils = jwtUtils;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;


    @Value("${jwt.expiration}")
    private Number JWT_EXPIRATION;

    @Override
    public Data signUp(SignUpDto signUpDto) {
        User duplicateUser = userDao.findByEmail(signUpDto);
        if (duplicateUser != null) {
            throw new BadRequestException("duplicateUser user.");
        }

        //return user
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setProvider(signUpDto.getProvider());
        user.setPicture("picture");

        //database insertion
        String hashedPassword = PasswordUtil.hashPassword(signUpDto.getPassword());
        signUpDto.setPassword(hashedPassword);
        User createdUser = userDao.createUser(signUpDto);

        if (createdUser == null) {
            throw new UnauthorizedException("signUp failed ");
        }

        //create JWT
        String token = jwtUtils.createJWT(
                createdUser.getId(),
                createdUser.getEmail(),
                createdUser.getName(),
                createdUser.getProvider(),
                createdUser.getPicture()
        );
        Data data = new Data();
        data.setAccessToken(token);
        data.setAccessExpired(JWT_EXPIRATION);
        data.setUser(createdUser);
        return data;
    }

    @Override
    public Data signIn(SignInDto signInDto) {
        User user = userDao.checkSignIn(signInDto);

        if (user == null || !PasswordUtil.checkPassword(signInDto.getPassword(), user.getPassword())) {
            throw new ForbiddenException("Invalid email or password");
        }

        String token = jwtUtils.createJWT(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProvider(),
                user.getPicture()
        );

        user.setProvider(user.getProvider());
        user.setPicture("picture");

        Data data = new Data();
        data.setAccessToken(token);
        data.setAccessExpired(JWT_EXPIRATION);
        data.setUser(user);
        return data;
    }


    @Override
    public Map<String, Object> getUserProfile(String accessToken) {
        try {
            Claims claims = jwtUtils.parseJWT(accessToken);
            ProfileResponse profileResponse = new ProfileResponse();
            profileResponse.setId(claims.get("id", Long.class));
            profileResponse.setName(claims.get("name", String.class));
            profileResponse.setEmail(claims.get("email", String.class));
            profileResponse.setProvider(claims.get("provider", String.class));
            profileResponse.setPicture(claims.get("picture", String.class));
            Map<String, Object> data = new HashMap<>();
            data.put("data", profileResponse);
            return data;
        } catch (Exception e) {
            throw new ForbiddenException("Invalid access token");
        }
    }
}









