package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;

        Mockito.doNothing().when(repository).deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class)
                .when(repository)
                .deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        /**
         * Verificar se o metodo deleteById foi chamado na acao acima
         */
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);

    }

    @Test
    public void deleteShouldThrowEmptyDataAccessExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(EmptyResultDataAccessException.class ,() -> {
            service.delete(nonExistingId);
        });

        /**
         * Verificar se o metodo deleteById foi chamado na acao acima
         */
        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);

    }

}
