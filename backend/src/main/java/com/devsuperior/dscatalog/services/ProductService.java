package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dtos.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.resources.exceptions.ResourceExceptionHandler;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<ProductDTO> findAll() {

		List<Product> list = repository.findAll();

		List<ProductDTO> listDTO = list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
		return listDTO;
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {

		Optional<Product> opt = repository.findById(id);
		Product entity = opt.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
		ProductDTO dto = new ProductDTO(entity, entity.getCategories());
		return dto;
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {

		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("Id not found" + id);
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());

		entity.getCategories().clear();
		dto.getCategories().forEach(cat -> {
			Category category = categoryRepository.getOne(cat.getId());
			entity.getCategories().add(category);
		});
	}

	public void delete(long id) {
		try {
		repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new EmptyResultDataAccessException("Nao encontrado", 0);
		}

	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Pageable pageable) {
		Page<Product> list = repository.findAll(pageable);

		return list.map(x -> new ProductDTO(x));
	}

}
