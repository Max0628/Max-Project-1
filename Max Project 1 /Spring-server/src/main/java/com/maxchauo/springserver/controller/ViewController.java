package com.maxchauo.springserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {

    @GetMapping("/admin/product.html")
    public String showUrl() {
        return "admin/InsertProduct";
    }

    @GetMapping("/admin/user/auth")
    public String facebookLogin() {
        return "admin/ThirdPartyAuthFb";
    }

    @GetMapping("/admin/campaign.html")
    public String campaign() {
        return "admin/Campaign";
    }

    @GetMapping("/admin/checkout.html")
    public String tapPay() {
        return "admin/TapPay";
    }

}