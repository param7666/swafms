package com.nt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.nt.entity.Product;
import com.nt.exception.InvalidDiscountException;
import com.nt.exception.InvalidPriceException;
import com.nt.exception.InvalidQuantityException;
import com.nt.repository.ProductRepository;

@Service

public class ProductServiceImpl implements IProductService {

	private ProductRepository repo;
	private Logger log=LoggerFactory.getLogger(ProductServiceImpl.class);
	
	public ProductServiceImpl(ProductRepository repo) {
		log.info("ProductServiceImpl.ProductServiceImpl()");
		this.repo=repo;
	}
	
	@Override
	public String createProduct(Product p) throws Exception {
		log.info("ProductServiceImpl.createProduct()");
		if(p==null) {
			log.error("Product is null ");
			throw new NullPointerException("Product is null");
		}
		if(p.getPrice()<=0) throw new InvalidPriceException("Price must be > 0");
		if(p.getDiscount()<=0 || p.getDiscount()>=100) throw new InvalidDiscountException("Discount must be > 0 or <100");
		if(p.getQty()<=0) throw new InvalidQuantityException("Quantity must be > 0");
	
		try {
		Long pid=repo.save(p).getPid();
		return "Product saved with id :: "+pid;
		} catch(Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	
	}

	@Override
	public Product getProductById(Long id) throws Exception {
		return repo.findById(id).orElseThrow(()-> new RuntimeException("Invalid Id"));
		
	}

	@Override
	public List<Product> getAllProducts() throws Exception {
		log.info("ProductServiceImpl.getAllProducts()");
		try {
			List<Product> products=repo.findAll();
			return products;
		}catch(Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	}

	@Override
	public Product updateProject(Long id, Product p) throws Exception {
		log.info("ProductServiceImpl.updateProject()");
		Product product=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid Product Id"));
		return product;

	}

	@Override
	public String deleteProduct(Long id) throws Exception {
		repo.deleteById(id);
		return "Product Deleted...";
	}

	@Override
	public List<Product> getProductByCategory(String category) throws Exception {
		return repo.getAllProductByCategory(category);
	}

}
