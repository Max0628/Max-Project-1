package com.maxchauo.springserver.service.user;

import com.maxchauo.springserver.dao.user.UserDao;
import com.maxchauo.springserver.dto.user.Data;
import com.maxchauo.springserver.dto.user.FbSignInDto;
import com.maxchauo.springserver.model.user.User;
import com.maxchauo.springserver.util.JWTutils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class FaceBookServiceImpl implements FaceBookService {


    @Value("${jwt.expiration}")
    private Long JWT_EXPIRATION;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JWTutils jwtUtils;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public Data loginWithFacebook(String facebookToken) {
        String facebookGraphApiUrl = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=" + facebookToken;
        FbSignInDto fbSignInDto = restTemplate.getForObject(facebookGraphApiUrl, FbSignInDto.class);

        User userInDatabase = userDao.checkFacebookEmail(fbSignInDto);

        if (userInDatabase == null) {
            userInDatabase = userDao.faceBookCreateUser(fbSignInDto);

        }

        String accessToken = jwtUtils.createJWT(userInDatabase.getId(), userInDatabase.getName(), userInDatabase.getEmail(), userInDatabase.getProvider(), userInDatabase.getPicture());

        User user = new User();
        user.setId(userInDatabase.getId());
        user.setName(userInDatabase.getName());
        user.setEmail(userInDatabase.getEmail());
        user.setProvider(userInDatabase.getProvider());
        user.setPicture(userInDatabase.getPicture());
        Data data = new Data();
        data.setAccessToken(accessToken);
        data.setUser(user);
        data.setAccessExpired(3600);
        return data;
    }
}
