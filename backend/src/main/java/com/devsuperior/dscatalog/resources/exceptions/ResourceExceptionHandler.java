package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		var httpStatus = HttpStatus.NOT_FOUND;
		err.setTimestamp(Instant.now());
		err.setStatus(httpStatus.value());
		err.setError("Resource not Found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(httpStatus).body(err);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		var httpStatus = HttpStatus.NOT_FOUND;
		err.setTimestamp(Instant.now());
		err.setStatus(httpStatus.value());
		err.setError("Entity not Found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(httpStatus).body(err);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		StandardError err = new StandardError();
		var httpStatus = HttpStatus.BAD_REQUEST;
		err.setTimestamp(Instant.now());
		err.setStatus(httpStatus.value());
		err.setError("Database exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(httpStatus).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		var err = new ValidationError();
		var httpStatus = HttpStatus.UNPROCESSABLE_ENTITY; //422 - alguma entidade não pode ser processada
		err.setTimestamp(Instant.now());
		err.setStatus(httpStatus.value());
		err.setError("Validation exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			err.addError(fieldError.getField(), fieldError.getDefaultMessage());
		});

		return ResponseEntity.status(httpStatus).body(err);
	}
	
}
