package com.nt.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nt.dto.ProductDto;
import com.nt.payload.ApiResponse;

@FeignClient(name = "ProductService", path = "/api/product")
public interface ProductFeingClient {

	@PostMapping("/create")
	public String createProduct(@RequestBody ProductDto p) throws Exception;
	
	@GetMapping("/{id}")
	public ApiResponse<ProductDto> getProductById(@PathVariable Long id) throws Exception;
	
	@GetMapping("/category/{category}")
	public ProductDto GetProductByCategory(@PathVariable String category) throws Exception;
	
	@DeleteMapping("/delete/{id}")
	public String deleteProductById(@PathVariable Long id) throws Exception;
	
	@PutMapping("/update/{id}")
	public ProductDto updateProduct(@PathVariable Long id,@RequestBody ProductDto p) throws Exception;
	
	@GetMapping("/all")
	public List<ProductDto> GetAllProduct() throws Exception;
}
