package com.alelo.desafiojava.api.model.repository;

import static com.alelo.desafiojava.util.UtilsTest.geraAutomovel;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alelo.desafiojava.api.model.entity.Automovel;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
class AutomovelRepositoryTest {

	@Autowired
	private TestEntityManager manager;
	
	@Autowired
	private AutomovelRepository repository;
	
	@Test
	@DisplayName("Deve salvar um automóvel")
	void salvaAutmovel() {
		
		Automovel automovel = geraAutomovel();
		
		Automovel automovelSalvo = repository.save(automovel);
		
		assertThat(automovelSalvo.getId()).isNotNull();
		
	}
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um automóvel na base com a placa informada")
	void retornaTrueQuandoJaExistirPlaca() {
		
		String placa = "BRA2E21";
		Automovel automovel = geraAutomovel();
		automovel.setId(null);
		automovel.setPlaca(placa);
		
		manager.persist(automovel);
		
		boolean placaExiste = repository.existsByPlaca(placa);
		
		assertThat(placaExiste).isTrue();
		
	}
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um automóvel na base com a placa informada")
	void retornaFalseQuandoNaoExistirPlaca() {
		
		String placa = "BRA2E21";
		
		boolean placaExiste = repository.existsByPlaca(placa);
		
		assertThat(placaExiste).isFalse();
		
	}
	
	@Test
	@DisplayName("Deve obter um automóvel pela placa")
	void buscaAutomovelPorPlaca() {
		
		Automovel automovel = geraAutomovel();
		automovel.setId(null);
		
		manager.persist(automovel);
		
		Optional<Automovel> automovelEncontrado = repository.findByPlacaContaining(automovel.getPlaca());
		
		assertThat(automovelEncontrado).isNotNull();
		
	}
	
	@Test
	@DisplayName("Deve excluir um automóvel")
	void excluiAutomovel() {
		Automovel automovel = geraAutomovel();
		automovel.setId(null);
		
		manager.persist(automovel);
		
		Automovel automovelEncontrado = manager.find(Automovel.class, automovel.getId());
		
		repository.delete(automovelEncontrado);
		
		Automovel automovelExcluido = manager.find(Automovel.class, automovel.getId());
		
		assertThat(automovelExcluido).isNull();
	}
	
}
