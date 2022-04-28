package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        //Arrange
        long existingId = 1L;

        //Act
        repository.deleteById(existingId);
        Optional<Product> result = repository.findById(existingId);

        //Assert
        Assertions.assertEquals(false,
                result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultNotFoundExceptionWhenIdDoesNotExists(){
        //Arrange
        long nonExistingId = 1000L;

        //Assert
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            //Act
            repository.deleteById(nonExistingId);
        });
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        //Arrange
        long lastId = 25L;
        var product = Factory.createProduct();

        //Assert
        product = repository.save(product);

        //Act
        Assertions.assertEquals(++lastId, product.getId());
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExists(){
        //Arrange
        long existingId = 1L;

        //Act
        var opt = repository.findById(existingId);

        //Assert
        Assertions.assertEquals(true, opt.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists(){
        //Arrange
        long nonExistingId = 10000L;

        //Act
        var opt = repository.findById(nonExistingId);

        //Assert
        Assertions.assertEquals(true, opt.isEmpty());
    }

}
