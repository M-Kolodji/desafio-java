package com.alelo.desafiojava.api.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.BindingResult;

public class ApiErrors {

	private List<String> erros;
	
	public ApiErrors(BindingResult result) {
		this.erros = new ArrayList<>();
		result.getAllErrors().forEach(error -> this.erros.add(error.getDefaultMessage()));
	}

	public ApiErrors(String mensagem) {
		this.erros = Arrays.asList(mensagem);
	}
	
	public List<String> getErros() {
		return erros;
	}
	
}
