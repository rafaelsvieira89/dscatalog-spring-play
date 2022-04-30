package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.UserDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
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
public class UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private RoleRepository RoleRepository;

	@Transactional(readOnly = true)
	public List<UserDTO> findAll() {

		List<User> list = repository.findAll();

		List<UserDTO> listDTO = list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
		return listDTO;
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {

		Optional<User> opt = repository.findById(id);
		User entity = opt.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		UserDTO dto = new UserDTO(entity, entity.getRoles());
		return dto;
	}

	@Transactional
	public UserDTO insert(UserDTO dto) {

		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
	}

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setEmail(dto.getEmail());
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());

		entity.getRoles().clear();
		dto.getRoles().forEach(cat -> {
			Role Role = RoleRepository.getOne(cat.getId());
			entity.getRoles().add(Role);
		});
	}

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
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);

		return list.map(x -> new UserDTO(x));
	}

}