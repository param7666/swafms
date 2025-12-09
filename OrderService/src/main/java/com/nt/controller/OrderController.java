package com.nt.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nt.dto.OrderRequestDTO;
import com.nt.dto.OrderResponseDTO;
import com.nt.enitity.Order;
import com.nt.payload.ApiResponse;
import com.nt.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

	private final IOrderService service;
	
	private final Logger log=LoggerFactory.getLogger(OrderController.class);
	
	@PostMapping("/create")
	public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDTO orderReq)  {
		log.info("OrderController.createOrder()");
		try {
			OrderResponseDTO orderResponse=service.createOrder(orderReq);
			return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Order Created Succesfully.", orderResponse));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderById(@PathVariable Long id) {
		log.info("OrderController.getOrderById()");
		try {
			OrderResponseDTO order=service.getOrderById(id);
			return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllOrders() {
		log.info("OrderController.getAllOrders");
		try {
			List<OrderResponseDTO> orders=service.getAllOrders();
			return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", orders));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}
	
	 @GetMapping("/user/{userId}")
	 public ResponseEntity<?> getOrdersByUser(@PathVariable Long userId) {
	     List<OrderResponseDTO> orders;
		try {
			orders = service.getOrdersByUserId(userId);
	        return ResponseEntity.ok(ApiResponse.success("User orders retrieved successfully", orders));
		} catch (Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
      
    }
	 

	 @PutMapping("/{id}/status")
	    public ResponseEntity<?> updateOrderStatus(
	            @PathVariable Long id,
	            @RequestParam Order.OrderStatus status) {
	        OrderResponseDTO order;
			try {
				order = service.updateOrderStatus(id, status);
				return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", order));
			} catch (Exception e) {
				log.error(e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

			}
	        
	    }
	    
	    @DeleteMapping("/cancel/{id}")
	    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
	        try {
				service.cancelOrder(id);
		        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", null));

			} catch (Exception e) {
				log.error(e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

			}
	    }

	 
	
}
