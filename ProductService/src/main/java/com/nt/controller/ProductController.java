package com.nt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nt.entity.Product;
import com.nt.service.IProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	private final IProductService service;
	private final Logger log=LoggerFactory.getLogger(ProductController.class);
	

	public ProductController(IProductService service) {
		log.info("ProductController.ProductController()");
		this.service=service;
	}
	
	
	@PutMapping("/create")
	public ResponseEntity<?> createProduct(@RequestBody Product p) {
		log.info("ProductController.createProduct()");
		
		if(p.getName()==null || p.getCategory()==null) {
			return new ResponseEntity<String>("Values Can not be null", HttpStatus.BAD_REQUEST);
		}
		try {
			String result=service.createProduct(p);
			log.info(result);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			
		}
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id) {
		log.info("ProductController.getProductById()");
		if(id<=0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inavlid id");
		try {
			Product p=service.getProductById(id);
			return new ResponseEntity<Product>(p,HttpStatus.OK);
		} catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
