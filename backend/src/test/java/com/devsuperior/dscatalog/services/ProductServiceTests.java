package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Teste unitario onde não são carregados outros componentes. Para isso será utilizado o Mockito
 * O Mockito irá mockar o comportamento destes outros componentes
 */
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    /* Nao colocamos AutoWired para não injetar o componente
     *  Neste caso injetamos o Mockito
     */
    @InjectMocks
    private ProductService service;

    /**
     * Sempre que criar um Mock deve ser criado o comportamento simulado dele
     */
    @Mock
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 4L;

        Mockito.doNothing().when(repository).deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class)
                .when(repository)
                .deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> service.delete(existingId));

        /*
          Verificar se o metodo deleteById foi chamado na acao acima
         */
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);

    }

    @Test
    public void deleteShouldThrowEntityNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(EntityNotFoundException.class ,() -> service.delete(nonExistingId));

        /*
          Verificar se o metodo deleteById foi chamado na acao acima
         */
        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);

    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenObjectAssociated(){
        Assertions.assertThrows(DatabaseException.class ,() -> service.delete(dependentId));

        /*
          Verificar se o metodo deleteById foi chamado na acao acima
         */
        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

}
