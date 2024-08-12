package com.maxchauo.springserver.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VariantDto {
    private Long productId;
    @JsonIgnore
    private String colorName;
    @JsonProperty("color_code")
    private String colorCode;
    private String size;
    private Long stock;
}
