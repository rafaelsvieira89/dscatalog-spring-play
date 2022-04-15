package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class CategoryRepositoryTests {
	
	
	private long existingId = 1L;
	private long nonExistingId = 1000L;
	private long countTotalCategories = 5L;
	
	@Autowired
	private CategoryRepository repository;
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		//Arrange
		
		
		//Act
		repository.deleteById(existingId);
		Optional<Category> result = repository.findById(existingId);
		
		//Assert
		Assertions.assertFalse(result.isPresent());		
		
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		//Arrange
		
		
		//Assert
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			//Act
			repository.deleteById(nonExistingId);
		});
		
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdNull() {
		
		//Arrange
		Category c = Factory.createCategory();
		
		//Act
		c = repository.save(c);
		
		//Assert
		Assertions.assertNotNull(c.getId());
		Assertions.assertEquals(++countTotalCategories, c.getId());
	}
	
}
