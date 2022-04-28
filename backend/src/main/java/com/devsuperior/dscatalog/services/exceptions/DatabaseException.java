package com.devsuperior.dscatalog.services.exceptions;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;


public class DatabaseException extends RuntimeException{

	public DatabaseException(String message) {
		super(message);
	}
}
