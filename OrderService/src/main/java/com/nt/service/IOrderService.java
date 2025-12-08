package com.nt.service;

import java.util.List;

import com.nt.dto.OrderItemDto;
import com.nt.dto.OrderRequestDTO;
import com.nt.dto.OrderResponseDTO;
import com.nt.enitity.Order;

public interface IOrderService {

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) throws Exception;
    public OrderResponseDTO getOrderById(Long id)throws Exception;
    public List<OrderResponseDTO> getAllOrders()throws Exception;
    public List<OrderResponseDTO> getOrdersByUserId(Long userId)throws Exception;
    public OrderResponseDTO updateOrderStatus(Long id, Order.OrderStatus status)throws Exception;
    public void cancelOrder(Long id)throws Exception;
}
