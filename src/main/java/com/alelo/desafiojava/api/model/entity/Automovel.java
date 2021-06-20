package com.alelo.desafiojava.api.model.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.alelo.desafiojava.api.enums.TipoCarroceria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Automovel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String placa;
	private String modelo;
	private String marca;
	private TipoCarroceria tipoCarroceria;
	private BigDecimal diaria;
	private BigDecimal valorSeguro;
	
}
