package com.alelo.desafiojava.api.dto;

import java.math.BigDecimal;

import org.springframework.lang.NonNull;

import com.alelo.desafiojava.api.enums.TipoCarroceria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutomovelDTO {

	@NonNull
	private String placa;
	@NonNull
	private String modelo;
	@NonNull
	private String marca;
	@NonNull
	private TipoCarroceria tipoCarroceria;
	@NonNull
	private BigDecimal diaria;
	@NonNull
	private BigDecimal valorSeguro;
	
}
