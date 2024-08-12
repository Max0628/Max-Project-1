package com.maxchauo.springserver.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.maxchauo.springserver.model.user.User;
import org.springframework.stereotype.Component;

@lombok.Data
@Component
@JsonPropertyOrder({"access_token","access_expired","user"})
public class Data {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("access_expired")
    private Number accessExpired;
    private User user;
}