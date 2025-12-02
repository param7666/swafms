package com.nt.service;

import java.util.List;

import com.nt.entity.Product;

public interface IProductService {

	public String createProduct(Product p) throws Exception;
	
	public Product getProductById(Long id) throws Exception;
	
	public List<Product> getAllProducts() throws Exception;
	
	public Product updateProject(Long id, Product p) throws Exception;
	
	public String deleteProduct(Long id) throws Exception;
	
	public List<Product> getProductByCategory(String category) throws Exception;
}
