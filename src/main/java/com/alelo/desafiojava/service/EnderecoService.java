package com.alelo.desafiojava.service;

import java.util.Optional;

import com.alelo.desafiojava.api.dto.EnderecoDTO;

public interface EnderecoService {

	Optional<EnderecoDTO> buscaPorCep(String cep); 
}
