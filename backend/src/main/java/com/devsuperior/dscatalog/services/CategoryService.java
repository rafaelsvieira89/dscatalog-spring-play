package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dtos.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		
		List<Category> list = repository.findAll(); 
		
		
		List<CategoryDTO> listDTO = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList()); 
		return listDTO;
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		
		Optional<Category> opt =  repository.findById(id) ; 
		CategoryDTO dto = new CategoryDTO(opt.orElseThrow(() -> new EntityNotFoundException("Entity not found")));
		 
		return dto;
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public void delete(long id) {
		try{
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e){
			throw new EntityNotFoundException("Entity not found");
		}catch (DataIntegrityViolationException e){
			throw new DatabaseException("Integrity violation.");
		}
		
		
	}
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		Page<Category> list = repository.findAll(pageable);
		
		return list.map(x -> new CategoryDTO(x));
	}
	
}
