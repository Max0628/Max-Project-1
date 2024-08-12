package com.maxchauo.springserver.service.user;


import com.maxchauo.springserver.dto.user.Data;

public interface FaceBookService {
    Data loginWithFacebook(String facebookToken);

}
