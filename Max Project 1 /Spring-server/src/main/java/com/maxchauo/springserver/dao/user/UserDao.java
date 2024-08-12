package com.maxchauo.springserver.dao.user;

import com.maxchauo.springserver.dto.user.FbSignInDto;
import com.maxchauo.springserver.dto.user.SignInDto;
import com.maxchauo.springserver.model.user.User;
import com.maxchauo.springserver.dto.user.SignUpDto;

public interface UserDao {
    User findByEmail(SignUpDto signUpDto);

    User createUser(SignUpDto signUpDto);

    User checkSignIn(SignInDto signInDto);

    User checkFacebookEmail(FbSignInDto fbSignInDto);

    User faceBookCreateUser(FbSignInDto fbSignInDto);
}
