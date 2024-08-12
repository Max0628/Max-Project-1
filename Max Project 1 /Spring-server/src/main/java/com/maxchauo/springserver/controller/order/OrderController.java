package com.maxchauo.springserver.controller.order;

import com.maxchauo.springserver.dto.order.OrderDto;
import com.maxchauo.springserver.dto.order.OrderInputDto;
import com.maxchauo.springserver.service.order.OrderService;
import com.maxchauo.springserver.service.order.PaymentService;
import com.maxchauo.springserver.util.JWTutils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/1.0/order")
public class OrderController {


    @Autowired
    private JWTutils jwtUtils;
    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/checkout")
    ResponseEntity<?> insertOrder(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody OrderInputDto orderInputDto) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing or invalid Authorization header"));
        }
        if (orderInputDto.getOrderDto() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Order data is missing"));
        }

        String accessToken = authorizationHeader.replace("Bearer ", "").trim();
        boolean haveStock = orderService.checkStock(orderInputDto);
        if (!haveStock) {
            ResponseEntity.badRequest().body(Map.of("error", "Stock not enough"));
        }

        Claims claims = jwtUtils.parseJWT(accessToken);
        Long userId = claims.get("id", Long.class);

        Long orderId = orderService.createOrder(orderInputDto, userId);

        orderService.creatRecipient(orderInputDto, orderId);
        orderService.createList(orderInputDto, orderId);

        Long totalAmount = orderInputDto.getOrderDto().getTotal();
        String prime = orderInputDto.getPrime();
        Long paymentStatus = paymentService.processPayment(prime, totalAmount);
        OrderDto orderDto = orderInputDto.getOrderDto();

        if (paymentStatus == 0) {
            Long paymentSuccess = 1L;
            boolean stockToUpdate = orderService.updateStock(orderDto);
            boolean statusToUpdate = orderService.updateStatus(orderId, paymentSuccess);
            if (stockToUpdate && statusToUpdate) {
                return ResponseEntity.ok(Map.of("data", Map.of("number", orderId)));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", Map.of("error", "Failed to update order status or stock")));
            }
        } else {
            Long paymentFailed = -1L;
            orderService.updateStatus(orderId, paymentFailed);
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(Map.of("error", "Payment failed"));
        }
    }
}

