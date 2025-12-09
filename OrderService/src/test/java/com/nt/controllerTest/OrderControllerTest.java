package com.nt.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.controller.OrderController;
import com.nt.dto.OrderItemDto;
import com.nt.dto.OrderRequestDTO;
import com.nt.dto.OrderResponseDTO;
import com.nt.enitity.Order.OrderStatus;
import com.nt.service.IOrderService;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

	@Autowired
	private MockMvc mock;
	
	@MockitoBean
	private IOrderService service;
	
	@Autowired
	private ObjectMapper op;
	
	@Test
	void testCreateOrderSuccess() throws Exception {
		OrderItemDto item=new OrderItemDto();
		item.setProductId(10L);
		item.setQty(2);
		item.setSubTotal(500.00);
		
		OrderRequestDTO req=new OrderRequestDTO();
		
		req.setUserId(1L);
		req.setOrderItems(List.of(item));
		req.setShippingAddress("HYD");
		
		OrderResponseDTO res = new OrderResponseDTO();
	    res.setId(100L);
	    res.setUserId(1L);
	    res.setOrderItems(List.of(item));
	    res.setTotalAmount(1000.0);
	    res.setStatus(OrderStatus.SHIPPED);
	    res.setShippingAddress("Hyderabad");
	    res.setOrderDate(LocalDateTime.now());
	    res.setUpdatedDate(LocalDateTime.now());
	
	   Mockito.when(service.createOrder(req)).thenReturn(res);
	   
	   mock.perform(post("/api/orders/create")
			   .contentType(MediaType.APPLICATION_JSON)
			   .content(op.writeValueAsString(res)))
	   .andExpect(status().isCreated())
	   .andExpect(jsonPath("$.success").value(true))
	   .andExpect(jsonPath("$.message").value("Order Created Succesfully."))
       .andExpect(jsonPath("$.data.id").value(100L))
       .andExpect(jsonPath("$.data.userId").value(1L))
       .andExpect(jsonPath("$.data.orderItems[0].productId").value(10L))
       .andExpect(jsonPath("$.data.totalAmount").value(1000.0))
       .andExpect(jsonPath("$.data.status").value("SHIPPED"))
       .andExpect(jsonPath("$.data.shippingAddress").value("HYD"));
	}
	

}
