package com.maxchauo.springserver.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.stereotype.Component;


//use for sign-up
@lombok.Data
@Component
@JsonPropertyOrder({"provider", "name", "email", "picture"})
public class ProfileResponse {
    @JsonIgnore
    private Long id;
    private String provider;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String picture;
}
