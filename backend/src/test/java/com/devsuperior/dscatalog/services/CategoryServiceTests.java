package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.CategoryRepository;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

	@InjectMocks
	private CategoryService service;
	
	@Mock
	private CategoryRepository repository;
	
	private long existingId;
	private long nonExistingId;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		
		//Quando eu chamar deleteById com Id existente, não vai fazer nada
		Mockito.doNothing().when(repository).deleteById(existingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
	}
	

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		//Arrange
		
		//Act
		
		
		//Assert
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		//verifica se o método abaixo foi chamado no teste acima
		Mockito.verify(repository).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldTheowExceptionWhenIdExists() {
		//Arrange
		
		//Act
		
		
		//Assert
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		//verifica se o método abaixo foi chamado no teste acima
		Mockito.verify(repository).deleteById(existingId);
	}
}
