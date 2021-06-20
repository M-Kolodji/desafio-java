package com.alelo.desafiojava.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alelo.desafiojava.api.exception.ApiErrors;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestControllerAdvice
public class ApplicationControllerAdvice {

	@ExceptionHandler(JsonMappingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleJsonMappingException(JsonMappingException ex) {
		return new ApiErrors(ex.getOriginalMessage());
	}
}
