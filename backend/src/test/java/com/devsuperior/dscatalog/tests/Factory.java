package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dtos.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;

public class Factory {

	public static Category createCategory() {
		return new Category(null, "Bikes");		
	}
	
	public static CategoryDTO createCategoryDTO() {
		return new CategoryDTO(createCategory());
	}
}
