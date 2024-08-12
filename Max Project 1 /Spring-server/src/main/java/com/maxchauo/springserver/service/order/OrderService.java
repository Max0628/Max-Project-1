package com.maxchauo.springserver.service.order;

import com.maxchauo.springserver.dto.order.OrderDto;
import com.maxchauo.springserver.dto.order.OrderInputDto;

import java.util.List;

public interface OrderService {

    boolean checkStock(OrderInputDto orderInputDto);

    Long createOrder(OrderInputDto orderInputDto, Long userId);

    List<Long> createList(OrderInputDto orderInputDto, Long orderId);

    Long creatRecipient(OrderInputDto orderInputDto, Long userId);

    boolean updateStock(OrderDto orderDto);

    boolean updateStatus(Long orderId, Long newStatus);

}
