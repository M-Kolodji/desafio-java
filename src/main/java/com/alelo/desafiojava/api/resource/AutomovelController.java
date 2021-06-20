package com.alelo.desafiojava.api.resource;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.alelo.desafiojava.api.dto.AutomovelDTO;
import com.alelo.desafiojava.api.model.entity.Automovel;
import com.alelo.desafiojava.service.AutomovelService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/automoveis")
@RequiredArgsConstructor
@Slf4j
public class AutomovelController {

	private final AutomovelService service;
	private final ModelMapper mapper;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@ApiOperation("Cria um automóvel")
	public AutomovelDTO salvar(@RequestBody @Validated AutomovelDTO automovelDTO) {
		log.info("Criando automóvel com placa: {}",automovelDTO.getPlaca()); 
		Automovel automovel = mapper.map(automovelDTO, Automovel.class);
		automovel = service.salvar(automovel);
		return mapper.map(automovel, AutomovelDTO.class);
	}
	
	@GetMapping
	@ApiOperation("Retorna lista de automóveis com paginação")
	public Page<AutomovelDTO> buscaAutomoveis(AutomovelDTO dto, Pageable pageRequest) {
		Automovel automovel = mapper.map(dto, Automovel.class);
		
		Page<Automovel> resultado = service.buscaAutomoveis(automovel, pageRequest);
		
		List<AutomovelDTO> automoveis = resultado.getContent()
				.stream()
					.map(entity -> mapper.map(entity, AutomovelDTO.class))
					.collect(Collectors.toList());
		
		return new PageImpl<>(automoveis, pageRequest, resultado.getTotalElements());
		
	}
	
	@GetMapping("/{placa}")
	@ApiOperation("Busca um automóvel pela placa")
	public AutomovelDTO buscaPelaPlaca(@PathVariable String placa) {
		Automovel automovel = service.buscaPelaPlaca(placa);
		return mapper.map(automovel, AutomovelDTO.class);
	}
	
	@DeleteMapping("/{placa}")
	@ResponseStatus(value = NO_CONTENT)
	@ApiOperation("Exclui um automóvel")
	public void excluiAutomovel(@PathVariable String placa) {
		service.excluiAutomovel(placa);
	}
	
	@PutMapping("/{placa}")
	@ApiOperation("Altera um automóvel")
	public Automovel alteraAutomovel(@PathVariable String placa, @RequestBody AutomovelDTO dto) {
		Automovel automovel = mapper.map(dto, Automovel.class);
		return service.alteraAutomovel(placa, automovel);
	}
	
}
