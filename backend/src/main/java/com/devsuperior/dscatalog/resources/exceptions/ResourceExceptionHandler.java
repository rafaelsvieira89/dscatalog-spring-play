package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError();
		var httpStatus = HttpStatus.NOT_FOUND;
		err.setTimestamp(Instant.now());
		err.setStatus(httpStatus.value());
		err.setError("Resource not Found");
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
	
}
