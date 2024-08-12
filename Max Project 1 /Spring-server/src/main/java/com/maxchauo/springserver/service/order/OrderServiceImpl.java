package com.maxchauo.springserver.service.order;

import com.maxchauo.springserver.dao.order.OrderDao;
import com.maxchauo.springserver.dto.order.ListDto;
import com.maxchauo.springserver.dto.order.OrderDto;
import com.maxchauo.springserver.dto.order.OrderInputDto;
import com.maxchauo.springserver.dto.order.RecipientDto;
import com.maxchauo.springserver.exception.BadRequestException;
import com.maxchauo.springserver.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PaymentService paymentService;

    @Override
    public boolean checkStock(OrderInputDto orderInputDto) {
        return orderDao.isStockAvailable(orderInputDto.getOrderDto());
    }

    @Override
    public Long createOrder(OrderInputDto orderInputDto, Long userId) {
        return orderDao.insertOrder(orderInputDto.getOrderDto(), userId);
    }

    @Override
    public List<Long> createList(OrderInputDto orderInputDto, Long orderId) {
        OrderDto orderDto = orderInputDto.getOrderDto();
        List<ListDto> listDtos = orderDto.getList();

        List<Long> productIds = orderDao.getProductIdByName(orderDto);
        if (productIds.isEmpty()) {
            throw new RuntimeException("No products found for the given order");
        }
        Long productId = productIds.get(0);
        List<Long> variantIds = orderDao.getVariantId(orderDto, productId);

        if (variantIds.isEmpty()) {
            throw new RuntimeException("No variants found for the given products");
        }
        List<Long> newIds = orderDao.insertList(orderDto, productIds.get(0), variantIds.get(0), orderId);
        return new ArrayList<>(newIds);
    }


    @Override
    public Long creatRecipient(OrderInputDto orderInputDto, Long orderId) {
        RecipientDto recipientDto = orderInputDto.getOrderDto().getRecipient();
        OrderDto orderDto = new OrderDto();
        orderDto.setRecipient(recipientDto);
        return orderDao.insertRecipient(orderDto, orderId);
    }


    @Override
    public boolean updateStock(OrderDto orderDto) {
        return orderDao.updateOrderStock(orderDto);
    }

    @Override
    public boolean updateStatus(Long orderId, Long newStatus) {
        return orderDao.updateOrderStatus(orderId, newStatus);
    }
}

