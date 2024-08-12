package com.maxchauo.springserver.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;
//Accept data from client when sign-up and sign-in(native)
@Component
@lombok.Data
public class SignUpDto {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String provider;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String picture;
}