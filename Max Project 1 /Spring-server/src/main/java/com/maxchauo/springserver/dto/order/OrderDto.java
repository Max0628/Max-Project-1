package com.maxchauo.springserver.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private String shipping;
    private String payment;
    private Long status;
    private Long subtotal;
    private Long freight;
    private Long total;
    private RecipientDto recipient;
    private List<ListDto> list;
}
