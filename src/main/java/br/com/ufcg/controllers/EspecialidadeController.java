package br.com.ufcg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.ufcg.domain.Especialidade;
import br.com.ufcg.services.EspecialidadeService;
import br.com.ufcg.util.response.Response;

@RestController
public class EspecialidadeController {

	@Autowired
	EspecialidadeService especialidadeService;

	@RequestMapping(value = "api/especialidade", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Response> cadastraEspecialidade(@RequestBody Especialidade especialidade) throws Exception {
		Response response;
		
		try {
			Especialidade retorno = especialidadeService.criarEspecialidade(especialidade);

			response = new Response("Especialidade Criada", HttpStatus.OK.value(), retorno);

			return new ResponseEntity<>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/api/especialidade", produces = "application/json")
	public @ResponseBody List<Especialidade> listaEspecialidades() {
		return especialidadeService.getEspecialidades();
	}
}
