package com.nt.service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.nt.client.InventoryFeignClient;
import com.nt.client.PaymentFeignClient;
import com.nt.client.ProductFeingClient;
import com.nt.client.UserFeingClient;
import com.nt.dto.InventoryDTO;
import com.nt.dto.OrderItemDto;
import com.nt.dto.OrderRequestDTO;
import com.nt.dto.OrderResponseDTO;
import com.nt.dto.PaymentRequestDTO;
import com.nt.dto.PaymentResponseDTO;
import com.nt.dto.ProductDto;
import com.nt.dto.UserDTO;
import com.nt.enitity.Order;
import com.nt.enitity.Order.OrderStatus;
import com.nt.enitity.OrderItem;
import com.nt.payload.ApiResponse;
import com.nt.repository.OrderRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService{
	
	private final OrderRepository repo; 

	private final Logger log= LoggerFactory.getLogger(OrderServiceImpl.class);
	
	private final ProductFeingClient productClient;
	
	private final UserFeingClient userClient;
	
	private final InventoryFeignClient inventryClient;
	
	private final PaymentFeignClient paymentClient;
	
	@Override
	public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) throws Exception {
		log.info("Creating order for user: {}", orderRequest.getUserId());
		ApiResponse<UserDTO> userResponse= userClient.getUserById(orderRequest.getUserId());
		 if (userResponse.getData() == null) {
	            throw new RuntimeException("User not found");
	        }
		Order order = new Order();
		order.setUserId(orderRequest.getUserId());
		order.setShippingAddress(orderRequest.getShippingAddress());
		order.setStatus(Order.OrderStatus.PENDING);
		order.setOrderDate(LocalDateTime.now());
		order.setUpdatedDate(LocalDateTime.now());
		Double totalAmount=0.0;
		
		// Proccess Order Item
		for(OrderItemDto itemDTO:orderRequest.getOrderItems()) {
			ApiResponse<ProductDto> productResponse=productClient.getProductById(itemDTO.getId());
			ProductDto product=productResponse.getData();
			
			if(product==null) {
				log.error("Product not found in Create Order Service:: ");
				throw new RuntimeException("Product not found"+itemDTO.getProductName());
			}
			
			// check inventry
			ApiResponse<InventoryDTO> inventoryResponse=inventryClient.getInventoryByProductId(itemDTO.getProductId());
			InventoryDTO inventory=inventoryResponse.getData();
			
			if(inventory==null || inventory.getQuantity()<itemDTO.getQty()) {
				log.error("Insufficient inventory for product: " + product.getName());
				throw new RuntimeException("Insufficient inventory for product: " + product.getName());
			}
			
			// create Order Item
			OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getPid());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(itemDTO.getQty());
            orderItem.setPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice()*(itemDTO.getQty()));
            
            order.getOrderItems().add(orderItem);
            totalAmount = totalAmount+(orderItem.getSubtotal());
            
            // Reduce inventory
            //inventoryFeignClient.reduceInventory(itemDTO.getProductId(), itemDTO.getQuantity());
		}
		
        order.setTotalAmount(totalAmount);
        Order savedOrder = repo.save(order);
        
        // Process payment
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setOrderId(savedOrder.getId());
        paymentRequest.setUserId(savedOrder.getUserId());
        paymentRequest.setAmount(totalAmount);
        paymentRequest.setPaymentMethod("CREDIT_CARD");
        
        ApiResponse<PaymentResponseDTO> paymentResponse = paymentClient.processPayment(paymentRequest);
        
        if (paymentResponse.getData() != null && "SUCCESS".equals(paymentResponse.getData().getStatus())) {
            savedOrder.setStatus(Order.OrderStatus.CONFIRMED);
            savedOrder = repo.save(savedOrder);
        }
        
        log.info("Order created successfully: {}", savedOrder.getId());
        OrderResponseDTO orderResponse=new OrderResponseDTO();
        BeanUtils.copyProperties(savedOrder, orderResponse);
        return orderResponse;
    }
    
    public OrderResponseDTO createOrderFallback(OrderRequestDTO orderRequest, Exception ex) {
        log.error("Fallback method called for createOrder", ex);
        throw new RuntimeException("Service temporarily unavailable. Please try again later.");
	}

	@Override
	public OrderResponseDTO getOrderById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderResponseDTO> getAllOrders() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderResponseDTO> getOrdersByUserId(Long userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelOrder(Long id) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
