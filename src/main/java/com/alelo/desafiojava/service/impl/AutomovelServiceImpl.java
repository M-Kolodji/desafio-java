package com.alelo.desafiojava.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alelo.desafiojava.api.model.entity.Automovel;
import com.alelo.desafiojava.api.model.repository.AutomovelRepository;
import com.alelo.desafiojava.exception.EntidadeNaoEncontradaException;
import com.alelo.desafiojava.exception.NegocioException;
import com.alelo.desafiojava.service.AutomovelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutomovelServiceImpl implements AutomovelService {

	private final AutomovelRepository repository;
	
	@Override
	public Automovel salvar(Automovel automovel) {
		if(repository.existsByPlaca(automovel.getPlaca())) {
			throw new NegocioException("Placa já cadastrada");
		}
		return repository.save(automovel);
	}

	@Override
	public Page<Automovel> buscaAutomoveis(Automovel filter, Pageable pageRequest) {
		Example<Automovel> example = Example.of(filter, ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withIgnoreNullValues()
				.withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example , pageRequest);
	}

	@Override
	public Optional<Automovel> buscaPelaPlaca(String placa) {
		return repository.findByPlacaContaining(placa);
	}

	@Override
	public void excluiAutomovel(String placa) {
		Automovel automovel = buscaPelaPlaca(placa)
				.orElseThrow(() -> 
					new EntidadeNaoEncontradaException(String.format("Automóvel com placa %s não encontrado!", placa)));
		repository.delete(automovel);
	}

	@Override
	public Automovel alteraAutomovel(String placa, Automovel automovel) {
		Automovel automovelEncontrado = buscaPelaPlaca(placa)
				.orElseThrow(() -> 
				new EntidadeNaoEncontradaException(String.format("Automóvel com placa %s não encontrado!", placa)));
		BeanUtils.copyProperties(automovel, automovelEncontrado);
		return repository.save(automovelEncontrado);
	}

}
