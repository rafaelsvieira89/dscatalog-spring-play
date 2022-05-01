package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dtos.UserDTO;
import com.devsuperior.dscatalog.dtos.UserInsertDTO;
import com.devsuperior.dscatalog.dtos.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	/*
	   Como no AppConfig está marcado como @Bean o spring vai injetar o método aqui
	   por conta do Tipo do dado e a marcação @Autowired
	 */
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
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
		UserDTO dto = new UserDTO(entity);
		return dto;
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {

		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword())); //Trafega somente no serviço de inserção
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
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
		dto.getRoles().forEach(r -> {
			var role = RoleRepository.getOne(r.getId());
			entity.getRoles().add(role);
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

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		var user = repository.findByEmail(s);
		if(user == null){
			logger.error("User not found: "  + s);
			throw new UsernameNotFoundException("E-mail not found");
		}
		logger.info("User found " + s);
		return user;
	}
}
