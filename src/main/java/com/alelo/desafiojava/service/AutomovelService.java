package com.alelo.desafiojava.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alelo.desafiojava.api.model.entity.Automovel;

public interface AutomovelService {

	Automovel salvar(Automovel automovel);

	Page<Automovel> buscaAutomoveis(Automovel filter, Pageable pageRequest);

	Automovel buscaPelaPlaca(String placa);

	void excluiAutomovel(String placa);

	Automovel alteraAutomovel(String placa, Automovel automovel);
	
}
