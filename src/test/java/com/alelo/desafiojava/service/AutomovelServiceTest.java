package com.alelo.desafiojava.service;

import static com.alelo.desafiojava.util.UtilsTest.geraAutomovel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alelo.desafiojava.api.model.entity.Automovel;
import com.alelo.desafiojava.api.model.repository.AutomovelRepository;
import com.alelo.desafiojava.exception.EntidadeNaoEncontradaException;
import com.alelo.desafiojava.exception.NegocioException;
import com.alelo.desafiojava.service.impl.AutomovelServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class AutomovelServiceTest {

	private AutomovelService service;
	
	@MockBean
	private AutomovelRepository repository;
	
	@BeforeEach
	void setup() {
		this.service = new AutomovelServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Deve salvar um automóvel")
	void salvaAutomovelTest() {
		
		Automovel automovel = geraAutomovel();
		
		Automovel automovelSalvo = automovel;
		automovelSalvo.setId(1L);
		
		when(repository.save(automovel)).thenReturn(automovelSalvo);
		
		Automovel automovelRetornado = service.salvar(automovel);
		
		assertThat(automovelRetornado.getId()).isNotNull();
		assertThat(automovelRetornado.getPlaca()).isEqualTo(automovel.getPlaca());
		assertThat(automovelRetornado.getModelo()).isEqualTo(automovel.getModelo());
		assertThat(automovelRetornado.getMarca()).isEqualTo(automovel.getMarca());
		
	}
	
	@Test
	@DisplayName("Deve lançar erro de negócio ao tentar salvar um automóvel com placa duplicada")
	void naoDeveSalvarAutomovelComPlacaDuplicada() {
		
		Automovel automovel = geraAutomovel();
		
		when(repository.existsByPlaca(anyString())).thenReturn(true);
		
		Throwable exception = Assertions.catchThrowable(() -> service.salvar(automovel));
		
		assertThat(exception)
		.isInstanceOf(NegocioException.class)
		.hasMessage("Placa já cadastrada");
	
		verify(repository, never()).save(automovel);
	}

	@Test
	@DisplayName("Deve lista automóveis com paginação")
	void buscaAutomoveis() {
		
		Automovel automovel = geraAutomovel();
		PageRequest pageRequest = PageRequest.of(0, 10);
		List<Automovel> automoveis = Arrays.asList(automovel);
		Page<Automovel> page = new PageImpl<Automovel>(automoveis, pageRequest, 1);
		
		when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
			.thenReturn(page);
		
		Page<Automovel> resultado = service.buscaAutomoveis(automovel , pageRequest);
		
		assertThat(resultado.getTotalElements()).isEqualTo(1);
		assertThat(resultado.getContent()).isEqualTo(automoveis);
		assertThat(resultado.getPageable().getPageNumber()).isZero();
		assertThat(resultado.getPageable().getPageSize()).isEqualTo(10);
		
	}

	@Test
	@DisplayName("Deve obter automóvel pela placa informada")
	void buscaAutomovelPelaPlaca() {
		Automovel autmovel = geraAutomovel();
		
		when(repository.findByPlacaContaining(anyString())).thenReturn(Optional.of(autmovel));
		
		Automovel autmovelEncontrado = service.buscaPelaPlaca(autmovel.getPlaca()).get();
		
		assertThat(autmovelEncontrado.getId()).isEqualTo(autmovel.getId());
		assertThat(autmovelEncontrado.getPlaca()).isEqualTo(autmovel.getPlaca());
		assertThat(autmovelEncontrado.getMarca()).isEqualTo(autmovel.getMarca());
		assertThat(autmovelEncontrado.getModelo()).isEqualTo(autmovel.getModelo());
		assertThat(autmovelEncontrado.getTipoCarroceria()).isEqualTo(autmovel.getTipoCarroceria());
		assertThat(autmovelEncontrado.getValorSeguro()).isEqualTo(autmovel.getValorSeguro());
		assertThat(autmovelEncontrado.getDiaria()).isEqualTo(autmovel.getDiaria());
		
	}
	
	@Test
	@DisplayName("Deve obter vazio ao buscar um automóvel por uma placa inexistente")
	void buscaAutomovelPorPlacaInexistente() {
		
		when(repository.findByPlacaContaining(anyString())).thenReturn(Optional.empty());
		
		Optional<Automovel> automovel = service.buscaPelaPlaca("BRA2E21");
		
		assertThat(automovel).isNotPresent();
		
	}
	
	@Test
	@DisplayName("Deve excluir um automóvel")
	void excluiAutomovel() {
		
		Automovel automovel = geraAutomovel();
		
		when(service.buscaPelaPlaca(anyString())).thenReturn(Optional.of(automovel));
		
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.excluiAutomovel(automovel.getPlaca()));
		
		verify(repository).delete(automovel);
		
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar excluir um automóvel inexistente")
	void excluiAutomovelInexistente() {
		
		Automovel automovel = geraAutomovel();
		
		Throwable exception = Assertions.catchThrowable(() -> service.excluiAutomovel(automovel.getPlaca()));
		
		assertThat(exception)
		.isInstanceOf(EntidadeNaoEncontradaException.class)
		.hasMessage(String.format("Automóvel com placa %s não encontrado!", automovel.getPlaca()));
		
		verify(repository, never()).delete(automovel);
		
	}
	
}
