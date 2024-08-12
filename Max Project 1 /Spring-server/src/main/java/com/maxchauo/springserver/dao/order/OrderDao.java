package com.maxchauo.springserver.dao.order;

import com.maxchauo.springserver.dto.order.OrderDto;

import java.util.List;

public interface OrderDao {

    boolean isStockAvailable(OrderDto orderDto);

    List<Long> getVariantId(OrderDto orderDto, Long productId);

    List<Long> getProductIdByName(OrderDto orderDto);

    Long insertOrder(OrderDto orderDto, Long userId);

    Long insertRecipient(OrderDto orderDto, Long orderId);

    List<Long> insertList(OrderDto orderDto, Long productId, Long variantId, Long orderId);


    boolean updateOrderStock(OrderDto orderDto);

    boolean updateOrderStatus(Long orderId, Long newStatus);
}
