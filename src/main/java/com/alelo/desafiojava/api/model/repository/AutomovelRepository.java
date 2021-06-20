package com.alelo.desafiojava.api.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alelo.desafiojava.api.model.entity.Automovel;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {

	boolean existsByPlaca(String placa);
	
	Optional<Automovel> findByPlacaContaining(String placa);

}
