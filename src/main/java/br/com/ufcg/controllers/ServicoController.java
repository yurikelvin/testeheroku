package br.com.ufcg.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.ufcg.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.ufcg.dao.ServicoDAO;
import br.com.ufcg.domain.Cliente;
import br.com.ufcg.domain.Fornecedor;
import br.com.ufcg.domain.Servico;
import br.com.ufcg.dto.ServicoDTO;
import br.com.ufcg.services.ServicoService;
import br.com.ufcg.services.UsuarioService;
import br.com.ufcg.util.response.Response;

@RestController
public class ServicoController {

	@Autowired
	private ServicoService servicoService;

	@Autowired
	private UsuarioService usuarioService;

	@RequestMapping(value = "/api/servicos/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Response> getServicoById(@PathVariable Long id) {
		Response response;

		try {
			Servico servico = servicoService.getServicoByID(id);
			
			response = new Response("Serviço solicitado", HttpStatus.OK.value(), servico.toDAO());
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/servicos/fornecedor/historico", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Response> historicoServicoFornecedor(HttpServletRequest request) {

		Response response;

		try {
			Fornecedor fornecedor = (Fornecedor) request.getAttribute("user");
			List<Servico> servicosParticipados = servicoService.getServicosDoFornecedor(fornecedor);

			if (servicosParticipados.isEmpty()) {
				response = new Response("Voce ainda nao participou de nenhum servico", HttpStatus.OK.value());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			response = new Response("Servicos que voce participou", HttpStatus.OK.value(),
					servicoService.ordenaServicosPorData(servicosParticipados));
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/servicos/cliente/historico", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Response> historicoServicoCliente(HttpServletRequest request) {

		Response response;

		try {
			Cliente cliente = (Cliente) request.getAttribute("user");

			List<Servico> servicosParticipados = servicoService.getServicosCliente(cliente);

			if (servicosParticipados.isEmpty()) {
				response = new Response("Voce ainda nao participou de nenhum servico", HttpStatus.OK.value());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			response = new Response("Servicos que voce participou", HttpStatus.OK.value(),
					servicoService.ordenaServicosPorData(servicosParticipados));
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/servicos/fornecedor/cancelar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Response> cancelarServicoFornecedor(HttpServletRequest request,
			@RequestBody Servico servico) {

		Response response;

		try {
			Fornecedor fornecedor = (Fornecedor) request.getAttribute("user");

			Servico servicoEncontrado = servicoService.getServicoByID(servico.getId());

			Servico servicoAtualizado = servicoService.cancelarServicoFornecedor(servicoEncontrado, fornecedor);

			response = new Response("Servico cancelado com sucesso!", HttpStatus.OK.value(), servicoAtualizado.toDAO());
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/servicos/fornecedor/concluir", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Response> concluirServicoFornecedor(HttpServletRequest request,
			@RequestBody Servico servico) {
		Response response;

		try {
			Fornecedor fornecedor = (Fornecedor) request.getAttribute("user");
			Servico servicoAtualizado = servicoService.getServicoByID(servico.getId());
			servicoAtualizado = servicoService.concluirServico(servicoAtualizado, fornecedor);

			response = new Response("Servico concluido com sucesso!", HttpStatus.OK.value(), servicoAtualizado.toDAO());
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/servicos/cliente/cancelar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Response> cancelarServicoCliente(HttpServletRequest request, @RequestBody Servico servico) {
		Response response;

		try {
			Cliente cliente = (Cliente) request.getAttribute("user");
			Servico servicoAtualizado = servicoService.getServicoByID(servico.getId());
			servicoAtualizado = servicoService.cancelarServicoCliente(servicoAtualizado, cliente);

			response = new Response("Servico cancelado com sucesso!", HttpStatus.OK.value(), servicoAtualizado.toDAO());
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/cliente/servicos/{servicoId}/ofertas/{ofertaId}/aceitar", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Response> aceitarOferta(HttpServletRequest request, @PathVariable("servicoId") String servicoStringId, @PathVariable("ofertaId") String ofertaStringId) {
		Response response;

		try {
			Long servicoId = Long.parseLong(servicoStringId);
			Long ofertaId = Long.parseLong(ofertaStringId);
			Cliente cliente = (Cliente) request.getAttribute("user");

			Servico servico = servicoService.getServicoByID(servicoId);

			if(!cliente.getEmail().equals(servico.getCliente().getEmail())) {
				response = new Response("Você só pode aceitar uma oferta de um serviço que você criou!", HttpStatus.BAD_REQUEST.value());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			Oferta oferta = servicoService.getOfertaNoServico(servicoId, ofertaId);
			Servico servicoAtualizado = servicoService.aceitarOferta(servico, oferta);

			response = new Response("A Oferta escolhida será a Oferta final do Serviço!", HttpStatus.OK.value(),
					servicoAtualizado.toDAO());
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/servicos/fornecedor/aceitos", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Response> getServicoFornecedor(HttpServletRequest request) {
		Response response;

		try {
			Fornecedor fornecedor = (Fornecedor) request.getAttribute("user");
			List<Servico> servicosAceitos = servicoService.getServicosAceitosDoFornecedor(fornecedor);
			List<ServicoDAO> servicosOrdenados = servicoService.ordenaServicosPorData(servicosAceitos);
			response = new Response("Servicos que o fornecedor aceitou!", HttpStatus.OK.value(), servicosOrdenados);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
	}

	@RequestMapping(value = "/api/servicosCadastrados/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Response> getServicos() {
		Response response;

		try {
			List<Servico> servicosCadastrados = servicoService.getAll();
			List<ServicoDAO> servicosOrdenados = servicoService.ordenaServicosPorData(servicosCadastrados);

			response = new Response("Servicos cadastrados no sistema", HttpStatus.OK.value(), servicosOrdenados);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/servicos/cliente", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Response> getServicosFromCliente(HttpServletRequest request) {

		Response response;

		try {
			Cliente cliente = (Cliente) request.getAttribute("user");
			List<Servico> servicosDoCliente = servicoService.getServicosClienteEmProgresso(cliente);
			List<ServicoDAO> servicosOrdenados = servicoService.ordenaServicosPorData(servicosDoCliente);

			response = new Response("Servicos em aberto do cliente", HttpStatus.ACCEPTED.value(), servicosOrdenados);

			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/servicos/cliente", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Response> cadastrarServico(HttpServletRequest request, @RequestBody Servico servico) {

		Response response;

		try {
			Cliente cliente = (Cliente) request.getAttribute("user");
			cliente = (Cliente) usuarioService.getById(cliente.getId());

			Servico servicoCadastrado = servicoService.criarServico(cliente, servico);

			response = new Response("Serviço cadastrado com sucesso !", HttpStatus.OK.value(),
					servicoCadastrado.toDAO());
			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/api/servicos/fornecedor", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<Response> getServicosDisponiveisForThisFornecedor(HttpServletRequest request) {

		Response response;

		try {
			Fornecedor fornecedor = (Fornecedor) request.getAttribute("user");
			List<Servico> servicosDoFornecedor = servicoService.getServicosDisponiveisFornecedor(fornecedor);

			if (!servicosDoFornecedor.isEmpty()) {
				List<ServicoDAO> servicosOrdenados = servicoService.ordenaServicosPorData(servicosDoFornecedor);
				response = new Response("Servicos em aberto disponiveis para voce", HttpStatus.ACCEPTED.value(),
						servicosOrdenados);
				return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
			}

			response = new Response("Nao encontramos servicos disponiveis para voce", HttpStatus.ACCEPTED.value());
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/api/fornecedor/servicos/{servicoId}/ofertas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ResponseEntity<Response> cadastraOfertaNoServico(HttpServletRequest request, @PathVariable("servicoId") String servicoStringId, @RequestBody Oferta oferta) {
		Response response;

		try {
			Long servicoId = Long.parseLong(servicoStringId);
			Fornecedor fornecedor = (Fornecedor) request.getAttribute("user");
			oferta.setFornecedor(fornecedor);
			Servico servico = this.servicoService.adicionarOfertaNoServico(servicoId, oferta);
			response = new Response("Oferta cadastrada com sucesso!", HttpStatus.CREATED.value(),
					servico);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			response = new Response(e.getMessage(), HttpStatus.BAD_REQUEST.value());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
