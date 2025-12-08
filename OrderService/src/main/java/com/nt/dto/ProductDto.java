package com.nt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

	
	private long pid;
	
	private String name;
	
	
	private Double price;
	
	
	private String category;
	
	
	private double discount;
	
	
	private int qty;
	
}
