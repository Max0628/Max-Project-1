package com.maxchauo.springserver.dto.order;

import lombok.Data;

@Data
public class RecipientDto {
    private String name;
    private String phone;
    private String email;
    private String address;
    private String time;
}
