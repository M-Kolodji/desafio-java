package com.alelo.desafiojava.api.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import com.alelo.desafiojava.api.dto.EnderecoDTO;

@Component
@FeignClient(name = "Endereco", url = "${url.viacep}")
public interface EnderecoFeignClient {

	@GetMapping("/{cep}/json")
	EnderecoDTO getEndereco(String cep);	
	
}
