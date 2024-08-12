package com.maxchauo.springserver.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderInputDto {
    private String prime;
    @JsonProperty("order")
    private OrderDto orderDto;
}
