package com.maxchauo.springserver.dto.product;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ImageDto {
    private String url;
    private Long productId;
}
