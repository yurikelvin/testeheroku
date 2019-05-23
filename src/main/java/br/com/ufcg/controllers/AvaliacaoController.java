package br.com.ufcg.controllers;

import javax.servlet.http.HttpServletRequest;

import br.com.ufcg.domain.Avaliacao;
import br.com.ufcg.domain.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ufcg.domain.Usuario;
import br.com.ufcg.dto.AvaliacaoDTO;
import br.com.ufcg.services.AvaliacaoService;
import br.com.ufcg.services.ServicoService;
import br.com.ufcg.services.UsuarioService;
import br.com.ufcg.util.response.Response;

import java.util.HashMap;

@RestController
public class AvaliacaoController {

	@Autowired
	AvaliacaoService avaliacaoService;
	
	@Autowired
	ServicoService servicoService;
	
	@Autowired
	UsuarioService usuarioService;
	
	 @PostMapping(value = "/api/usuarios/servicos/{servicoId}/avaliacao/avaliar")
	    public @ResponseBody ResponseEntity<Response> avaliarUsuario(HttpServletRequest request, @PathVariable String servicoId, @RequestBody Avaliacao avaliacao) {
	    	Response response;
	    	
	    	try {
                Long servicoIdL = Long.parseLong(servicoId);
	    		Usuario avaliador = (Usuario) request.getAttribute("user");
	    		Servico servico = servicoService.getServicoByID(servicoIdL);
	    		AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO(avaliacao, servico);
	    		avaliacaoService.avaliarUsuario(avaliador,  avaliacaoDTO);
	    		response = new Response("O usuário foi avaliado com sucesso!", HttpStatus.OK.value(), avaliacaoDTO.getAvaliacao().toDAO());
	    		return new ResponseEntity<>(response, HttpStatus.OK);
	    	} catch(Exception e) {
	    		response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
	    		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    	}
	    }
	    
	    
	    @PostMapping(value = "/api/usuarios/avaliacao/get", produces = "application/json")
	    public @ResponseBody ResponseEntity<Response> getAvaliacaoMedia(@RequestBody String login) {
	    	Response response;
	    	
	    	try {
	    		Usuario user = usuarioService.getByLogin(login.toLowerCase());
	    		Double mediaAvaliacoes = avaliacaoService.calcularAvaliacaoMedia(user);
	    		response = new Response("Média das avaliações calculada com sucesso!", HttpStatus.OK.value(), mediaAvaliacoes);
	    		return new ResponseEntity<>(response, HttpStatus.OK);
	    	} catch(Exception e) {
	    		response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
	    		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    	}
	    }

		@RequestMapping(value = "/api/usuarios/avaliacao", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
        public @ResponseBody ResponseEntity<Response> getAvaliacoesMedia(@RequestParam("logins") String[] logins){
            Response response;
             try {
                 HashMap<String, Double> loginsComAvaliacao = new HashMap<>();
                 for(String login: logins) {
                     Usuario user = usuarioService.getByLogin(login.toLowerCase());
                     Double mediaAvaliacoes = avaliacaoService.calcularAvaliacaoMedia(user);
                     loginsComAvaliacao.put(login, mediaAvaliacoes);
                 }

                 response = new Response("Média das avaliações calculada com sucesso!", HttpStatus.OK.value(), loginsComAvaliacao);
                 return new ResponseEntity<>(response, HttpStatus.OK);
             } catch(Exception e) {
                 response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
                 return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
             }

        }


	    
	    
}
