package com.alelo.desafiojava.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alelo.desafiojava.api.dto.EnderecoDTO;
import com.alelo.desafiojava.api.feignClient.EnderecoFeignClient;
import com.alelo.desafiojava.service.EnderecoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnderecoServiceImpl implements EnderecoService {

	private final EnderecoFeignClient feignClient;

	@Override
	public Optional<EnderecoDTO> buscaPorCep(String cep) {
		return Optional.of(feignClient.getEndereco(cep));
	}
	
	
}
