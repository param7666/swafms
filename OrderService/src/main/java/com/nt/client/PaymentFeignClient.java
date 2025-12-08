package com.nt.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nt.dto.PaymentRequestDTO;
import com.nt.dto.PaymentResponseDTO;
import com.nt.payload.ApiResponse;

@FeignClient(name = "payment-service")
public interface PaymentFeignClient {
	
    @PostMapping("/api/payments/process")
    ApiResponse<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO paymentRequest);
}
