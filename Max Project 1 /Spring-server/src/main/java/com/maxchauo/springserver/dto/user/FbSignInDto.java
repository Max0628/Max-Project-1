package com.maxchauo.springserver.dto.user;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class FbSignInDto {
    private String id;
    private String name;
    private String email;
    private Picture picture;
    @Data
    public static class Picture {
        private Data data;
        @lombok.Data
        public static class Data {
            private String url;

        }
    }
}
