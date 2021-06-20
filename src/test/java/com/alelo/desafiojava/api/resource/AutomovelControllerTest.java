package com.alelo.desafiojava.api.resource;

import static com.alelo.desafiojava.util.UtilsTest.geraAutomovel;
import static com.alelo.desafiojava.util.UtilsTest.geraAutomovelDTO;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.alelo.desafiojava.api.dto.AutomovelDTO;
import com.alelo.desafiojava.api.enums.TipoCarroceria;
import com.alelo.desafiojava.api.model.entity.Automovel;
import com.alelo.desafiojava.exception.EntidadeNaoEncontradaException;
import com.alelo.desafiojava.exception.NegocioException;
import com.alelo.desafiojava.service.AutomovelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(AutomovelController.class)
@AutoConfigureMockMvc
class AutomovelControllerTest {

	private static final String AUTOMOVEL_API = "/api/automoveis";
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private AutomovelService service;

	@Test
	@DisplayName("Deve salvar um automóvel")
	void salvaAutomovel() throws Exception {
		
		AutomovelDTO automovelDTO = geraAutomovelDTO();
		
		String json = new ObjectMapper().writeValueAsString(automovelDTO);
		
		when(service.salvar(any(Automovel.class))).thenReturn(geraAutomovel());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
			.post(AUTOMOVEL_API)
			.accept(APPLICATION_JSON)
			.contentType(APPLICATION_JSON)
			.content(json);
		
		mvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("modelo").value(automovelDTO.getModelo()))
			.andExpect(jsonPath("placa").value(automovelDTO.getPlaca()))
			.andExpect(jsonPath("marca").value(automovelDTO.getMarca()))
			.andExpect(jsonPath("tipoCarroceria").value(automovelDTO.getTipoCarroceria().name()))
			.andExpect(jsonPath("diaria").value(automovelDTO.getDiaria()))
			.andExpect(jsonPath("valorSeguro").value(automovelDTO.getValorSeguro()));
	}

	@Test
	@DisplayName("Deve retornar erro ao tentar salvar um automóvel inválido")
	void naoDeveCriarAutomovelInvalido() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(new AutomovelDTO());
		
		RequestBuilder request = MockMvcRequestBuilders
				.post(AUTOMOVEL_API)
				.accept(APPLICATION_JSON)
				.contentType(APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("erros", hasSize(1)));
	}

	@Test
	@DisplayName("Deve lançar erro ao tentar criar um automóvel com a palca utilizada por outro")
	void naoDeveCriarAutomovelComPlacaDuplicada() throws Exception {
		
		String mensagemErro = "Placa já cadastrada";
		
		String json = new ObjectMapper().writeValueAsString(geraAutomovelDTO());
		
		when(service.salvar(any(Automovel.class))).thenThrow(new NegocioException(mensagemErro));
		
		RequestBuilder request = MockMvcRequestBuilders
				.post(AUTOMOVEL_API)
				.accept(APPLICATION_JSON)
				.contentType(APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("erros", hasSize(1)))
			.andExpect(jsonPath("erros[0]").value(mensagemErro));
		
	}
	
	@Test
	@DisplayName("Deve retornar a lista de automóveis")
	void buscaAutomoveis() throws Exception {
		
		Automovel automovel = geraAutomovel();
		
		Mockito.when(service.buscaAutomoveis(Mockito.any(Automovel.class), Mockito.any(Pageable.class)))
			.thenReturn(new PageImpl<Automovel>(Arrays.asList(automovel), PageRequest.of(0, 100), 1));
		
		String queryString = String.format("?modelo=%smarca=%s&page=0&size=100", automovel.getModelo(), automovel.getMarca());
		
		RequestBuilder request = get(AUTOMOVEL_API.concat(queryString))
				.accept(APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("content", Matchers.hasSize(1)))
			.andExpect(jsonPath("totalElements").value(1))
			.andExpect(jsonPath("pageable.pageSize").value(100))
			.andExpect(jsonPath("pageable.pageNumber").value(0));
		
	}
	
	@Test
	@DisplayName("Deve obter informações de um automóvel quando informado a placa")
	void buscaAutomovelPorPlaca() throws Exception {
		Automovel automovel = geraAutomovel();
		AutomovelDTO dto = geraAutomovelDTO();
		
		String placa = automovel.getPlaca();
		
		when(service.buscaPelaPlaca(anyString())).thenReturn(Optional.of(automovel));
		
		RequestBuilder request = get(AUTOMOVEL_API.concat("/"+ placa )).accept(APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("placa").value(dto.getPlaca()))
			.andExpect(jsonPath("modelo").value(dto.getModelo()))
			.andExpect(jsonPath("marca").value(dto.getMarca()));
		
	}
	
	@Test
	@DisplayName("Deve lançar erro ao buscar placa de automóvel inexistente")
	void buscaAutomovelPorPlacaInexistente() throws Exception {
		
		when(service.buscaPelaPlaca(anyString())).thenReturn(Optional.empty());
		
		RequestBuilder request = get(AUTOMOVEL_API.concat("/BRA2E21" )).accept(APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(status().isNotFound());
		
	}
	
	@Test
	@DisplayName("Deve excluir um automóvel")
	void excluiAutomovel() throws Exception {
		
		RequestBuilder request = delete(AUTOMOVEL_API.concat("/BRA2E21"));
		
		mvc.perform(request)
			.andExpect(status().isNoContent());
		
	}
	
	@Test
	@DisplayName("Deve excluir um automóvel com placa inexistente")
	void excluiAutomovelComPlacaInexistente() throws Exception {
		
		when(service.buscaPelaPlaca(anyString())).thenThrow(new EntidadeNaoEncontradaException("Automóvel com placa BRA2E21 não encontrado!"));
		
		RequestBuilder request = delete(AUTOMOVEL_API.concat("/BRA2E21"));
		
		mvc.perform(request)
			.andExpect(status().isNoContent());
		
	}
	
	@Test
	@DisplayName("Deve alterar informações de um automóvel")
	void alteraAutomovel() throws Exception {
		
		String modelo = "Tracker";
		TipoCarroceria carroceria = TipoCarroceria.SUV;
		
		AutomovelDTO automovelDTO = geraAutomovelDTO();
		automovelDTO.setModelo(modelo);
		automovelDTO.setTipoCarroceria(carroceria);
		
		Automovel automovel = geraAutomovel();
		
		Automovel automovelAlterado = automovel;
		automovelAlterado.setId(1L);
		automovel.setModelo(modelo);
		automovel.setTipoCarroceria(carroceria);
		
		String json = new ObjectMapper().writeValueAsString(automovelDTO);
		
		when(service.buscaPelaPlaca(anyString())).thenReturn(Optional.of(automovel));
		when(service.alteraAutomovel(anyString(), Mockito.any(Automovel.class))).thenReturn(automovelAlterado);		
		
		RequestBuilder request = put(AUTOMOVEL_API.concat("/"+automovelDTO.getPlaca()))
				.accept(APPLICATION_JSON)
				.contentType(APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
			.andExpect(status().isOk())
			.andExpect(jsonPath("modelo").value(automovelDTO.getModelo()))
			.andExpect(jsonPath("placa").value(automovelDTO.getPlaca()))
			.andExpect(jsonPath("marca").value(automovelDTO.getMarca()))
			.andExpect(jsonPath("tipoCarroceria").value(automovelDTO.getTipoCarroceria().name()))
			.andExpect(jsonPath("diaria").value(automovelDTO.getDiaria()))
			.andExpect(jsonPath("valorSeguro").value(automovelDTO.getValorSeguro()));
			
	}
}
