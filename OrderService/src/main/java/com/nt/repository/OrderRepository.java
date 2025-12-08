package com.nt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.enitity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	 List<Order> findByUserId(Long userId);
	 List<Order> findByStatus(Order.OrderStatus status);
}
