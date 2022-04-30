package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.RoleDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

	@Autowired
	private RoleRepository repository;
	
	@Transactional(readOnly = true)
	public List<RoleDTO> findAll(){
		
		List<Role> list = repository.findAll(); 
		
		
		List<RoleDTO> listDTO = list.stream().map(x -> new RoleDTO(x)).collect(Collectors.toList()); 
		return listDTO;
	}
	
	@Transactional(readOnly = true)
	public RoleDTO findById(Long id){
		
		Optional<Role> opt =  repository.findById(id) ; 
		RoleDTO dto = new RoleDTO(opt.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
		 
		return dto;
	}

	@Transactional
	public RoleDTO insert(RoleDTO dto) {
		
		Role entity = new Role();
		entity.setAuthority(dto.getAuthority());
		entity = repository.save(entity);
		return new RoleDTO(entity);
	}
	
	@Transactional
	public void delete(long id) {
		try{
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e){
			throw new ResourceNotFoundException("Entity not found");
		}catch (DataIntegrityViolationException e){
			throw new DatabaseException("Integrity violation.");
		}
		
		
	}
	
	@Transactional(readOnly = true)
	public Page<RoleDTO> findAllPaged(Pageable pageable) {
		Page<Role> list = repository.findAll(pageable);
		
		return list.map(x -> new RoleDTO(x));
	}
	
}
