package com.maxchauo.springserver.dto.user;

import lombok.Data;

//這是用於接收來自前端的token的dto
@Data
public class FacebookLoginRequest {
    private String accessToken;
    private String provider;
}