package com.maxchauo.springserver.model.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.stereotype.Component;

@lombok.Data
@Component
@JsonPropertyOrder({"id", "provider", "name", "email", "picture"})
public class User {
    private Long id;
    private String provider;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String picture;
}
