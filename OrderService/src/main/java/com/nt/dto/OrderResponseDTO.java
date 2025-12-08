package com.nt.dto;

import java.time.LocalDateTime;
import java.util.*;

import com.nt.enitity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private List<OrderItemDto> orderItems;
    private Double totalAmount;
    private Order.OrderStatus status;
    private String shippingAddress;
    private LocalDateTime orderDate;
    private LocalDateTime updatedDate;
}