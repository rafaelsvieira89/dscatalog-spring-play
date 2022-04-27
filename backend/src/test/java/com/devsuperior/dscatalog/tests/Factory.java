package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dtos.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

	public static Product createProduct() {
		return new Product(null, "Audi A3", "Audi A3 1.8 TFSI", 93300.0, "audi.png", Instant.now());
	}

	public static Category createCategory() {
		return new Category(null, "Bikes");		
	}
	
	public static CategoryDTO createCategoryDTO() {
		return new CategoryDTO(createCategory());
	}
}
