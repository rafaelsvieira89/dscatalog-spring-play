package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

/**
 * Teste de integração
 * Como este é um teste de integração, podemos injetar o serviço e utilizar dados do banco de dados
 */
@SpringBootTest
@Transactional //desta maneira depois de cada execucao é dado rollback na transacao
public class ProductServiceIt {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists(){

        service.delete(existingId);

        Assertions.assertEquals(--countTotalProducts, repository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
    }

    @Test
    public void findAllPagedShouldReturnPagedWhenPage0Size10(){
        var pageRequest = PageRequest.of(0 ,10);
        var result = service.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        // numero da pagina
        Assertions.assertEquals(0, result.getNumber());

        //Tamanho da pagina
        Assertions.assertEquals(10, result.getSize());

        //Total de elementos
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPagedWhenPageDoesNotExist(){
        var pageRequest = PageRequest.of(50 ,10);
        var result = service.findAllPaged(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPagedWhenSortByName(){
        var pageRequest = PageRequest.of(0 ,10, Sort.by("name"));
        var result = service.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());

        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

}
