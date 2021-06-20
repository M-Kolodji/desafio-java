package com.alelo.desafiojava.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alelo.desafiojava.api.dto.EnderecoDTO;
import com.alelo.desafiojava.api.feignClient.EnderecoFeignClient;
import com.alelo.desafiojava.service.impl.EnderecoServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class EnderecoServiceTest {

	private EnderecoService service;
	
	@MockBean
	private EnderecoFeignClient feignClient; 
	
	@BeforeEach
	void setup() {
		this.service = new EnderecoServiceImpl(feignClient);
	}
	
	@Test
	@DisplayName("Busca endereço pelo cep informado")
	void buscaPorCep() {
		
		EnderecoDTO endereco = EnderecoDTO.builder()
				.cep("01001-000")
				.bairro("Praça da Sé")
				.complemento("lado ímpar")
				.ddd("Sé")
				.gia("São Paulo")
				.ibge("SP")
				.localidade("3550308")
				.logradouro("1004")
				.siafi("11")
				.uf("7107")
				.build();
		
		Mockito.when(feignClient.getEndereco(Mockito.anyString())).thenReturn(endereco);
		
		EnderecoDTO enderecoEncontrado = service.buscaPorCep(endereco.getCep()).get();
		
		assertThat(enderecoEncontrado.getCep()).isEqualTo(endereco.getCep());
		assertThat(enderecoEncontrado.getBairro()).isEqualTo(endereco.getBairro());
		assertThat(enderecoEncontrado.getComplemento()).isEqualTo(endereco.getComplemento());
		assertThat(enderecoEncontrado.getDdd()).isEqualTo(endereco.getDdd());
		assertThat(enderecoEncontrado.getGia()).isEqualTo(endereco.getGia());
		assertThat(enderecoEncontrado.getIbge()).isEqualTo(endereco.getIbge());
		assertThat(enderecoEncontrado.getLocalidade()).isEqualTo(endereco.getLocalidade());
		assertThat(enderecoEncontrado.getLogradouro()).isEqualTo(endereco.getLogradouro());
		assertThat(enderecoEncontrado.getSiafi()).isEqualTo(endereco.getSiafi());
		assertThat(enderecoEncontrado.getUf()).isEqualTo(endereco.getUf());

	}
	
}
