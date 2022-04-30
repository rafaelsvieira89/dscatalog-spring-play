package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dtos.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp(){
        existingId = 1L;
        nonExistingId = 2L;

        productDTO = Factory.createProductDTO();
        PageImpl<ProductDTO> page = new PageImpl<>(List.of(productDTO));

        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(service.findById(existingId)).thenReturn(productDTO);

        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
        Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{

        var jsonBody = objectMapper.writeValueAsString(productDTO);

        var result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        var jsonBody = objectMapper.writeValueAsString(productDTO);

        var result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status()
                        .isNotFound());
    }
}
