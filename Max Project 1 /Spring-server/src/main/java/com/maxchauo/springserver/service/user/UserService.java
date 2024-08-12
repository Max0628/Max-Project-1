package com.maxchauo.springserver.service.user;

import com.maxchauo.springserver.dto.user.Data;
import com.maxchauo.springserver.dto.user.SignInDto;
import com.maxchauo.springserver.dto.user.SignUpDto;

import java.util.Map;

public interface UserService {


    Data signUp(SignUpDto signUpDto);

    Data signIn(SignInDto signInDto);

    Map<String, Object> getUserProfile(String accessToken);


}
