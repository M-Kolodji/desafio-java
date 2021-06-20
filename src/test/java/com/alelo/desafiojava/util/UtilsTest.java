package com.alelo.desafiojava.util;

import java.math.BigDecimal;

import com.alelo.desafiojava.api.dto.AutomovelDTO;
import com.alelo.desafiojava.api.enums.TipoCarroceria;
import com.alelo.desafiojava.api.model.entity.Automovel;

public class UtilsTest {

	public static Automovel geraAutomovel() {
		return Automovel.builder()
				.id(1L)
				.placa("BRA2E21")
				.marca("GM")
				.modelo("Onix")
				.tipoCarroceria(TipoCarroceria.HATCH)
				.diaria(new BigDecimal(150.0))
				.valorSeguro(new BigDecimal(25.99))
				.build();
	}

	public static AutomovelDTO geraAutomovelDTO() {
		return AutomovelDTO.builder()
				.placa("BRA2E21")
				.marca("GM")
				.modelo("Onix")
				.tipoCarroceria(TipoCarroceria.HATCH)
				.diaria(new BigDecimal(150.0))
				.valorSeguro(new BigDecimal(25.99))
				.build();
	}
	
}
