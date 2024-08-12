package com.maxchauo.springserver.dto.order;

import com.maxchauo.springserver.dto.product.ColorDto;
import com.maxchauo.springserver.dto.product.SizeDto;
import lombok.Data;

@Data
public class ListDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long variantId;
    private String name;
    private Long price;
    private ColorDto color;
    private String size;
    private Long qty;
}
