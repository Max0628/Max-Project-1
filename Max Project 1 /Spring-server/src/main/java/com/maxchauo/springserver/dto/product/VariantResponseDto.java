package com.maxchauo.springserver.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonPropertyOrder({"color_code","size","stock"})
public class VariantResponseDto {
    private String size;
    @JsonProperty("color_code")
    private String colorCode;
    private Long stock;
}
