package com.maxchauo.springserver.controller.user;

import com.maxchauo.springserver.dto.user.Data;
import com.maxchauo.springserver.dto.user.FbSignInDto;
import com.maxchauo.springserver.dto.user.SignInDto;
import com.maxchauo.springserver.dto.user.SignUpDto;
import com.maxchauo.springserver.exception.BadRequestException;
import com.maxchauo.springserver.exception.ForbiddenException;
import com.maxchauo.springserver.exception.UnauthorizedException;
import com.maxchauo.springserver.service.user.FaceBookServiceImpl;
import com.maxchauo.springserver.service.user.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/1.0/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private FbSignInDto fbSignInDto;

    @Value("${jwt.expiration}")
    private String EXPIRE_TIME;
    @Autowired
    private SignInDto signInDto;
    @Autowired
    private FaceBookServiceImpl faceBookService;
    @Autowired
    private Data data;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        Data newUser = userService.signUp(signUpDto);
        Map<String, Data> response = new HashMap<>();
        response.put("data", newUser);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Map<String, Object> requestBody) {
        String provider = (String) requestBody.get("provider");
        switch (provider.toLowerCase()) {
            case "facebook":
                String accessToken = (String) requestBody.get("access_token");

                System.out.println(accessToken);//print out fb access token

                if (accessToken == null || accessToken.isEmpty()) {
                    throw new BadRequestException("Facebook access token is required");
                }
                Data facebookResponse = faceBookService.loginWithFacebook(accessToken);
                return ResponseEntity.ok(facebookResponse);

            case "native":
                String email = (String) requestBody.get("email");
                String password = (String) requestBody.get("password");
                if (email == null || email.isEmpty()) {
                    throw new BadRequestException("Email is mandatory");
                }
                if (password == null || password.isEmpty()) {
                    throw new BadRequestException("Password is mandatory");
                }

                SignInDto signInDto = new SignInDto();
                signInDto.setEmail(email);
                signInDto.setPassword(password);
                signInDto.setProvider(provider);

                Data userSignInSuccess = userService.signIn(signInDto);
                Map<String, Object> response = new HashMap<>();
                if (userSignInSuccess != null) {
                    response.put("data", userSignInSuccess);
                    return ResponseEntity.ok(response);
                } else {
                    throw new ForbiddenException("User sign in failed");
                }

            default:
                throw new BadRequestException("Unsupported provider");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("401 : Missing or invalid Authorization header");
        }
        String accessToken = authorizationHeader.replace("Bearer ", "").trim();
        Map data = userService.getUserProfile(accessToken);
        return ResponseEntity.ok(data);
    }
}


