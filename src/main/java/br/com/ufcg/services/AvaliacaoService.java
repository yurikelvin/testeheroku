package br.com.ufcg.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ufcg.domain.Avaliacao;
import br.com.ufcg.domain.Servico;
import br.com.ufcg.domain.Usuario;
import br.com.ufcg.domain.enums.TipoStatus;
import br.com.ufcg.domain.enums.TipoUsuario;
import br.com.ufcg.dto.AvaliacaoDTO;
import br.com.ufcg.repositories.AvaliacaoRepository;

@Service
public class AvaliacaoService {
	
	public static final double MEDIA_INICIAL = 5.0;
	
	@Autowired
	AvaliacaoRepository avaliacaoRepository;

	@Autowired
	ServicoService servicoService;

	@Autowired
	UsuarioService usuarioService;

	public Avaliacao addAvaliacao(Avaliacao avaliacao, Usuario usuario, Servico servico) throws Exception {
		if (avaliacao.getNota() >= 0 && avaliacao.getNota() <= 5) {
			avaliacao.setServico(servico);
			avaliacao.setUsuario(usuario);

			return avaliacaoRepository.save(avaliacao);
		}

		throw new Exception("A nota tem que ser entre 0 e 5!");
	}

	public Avaliacao getAvaliacao(Long id) {
		return avaliacaoRepository.find(id);
	}

	public Double calcularAvaliacaoMedia(Set<Avaliacao> avaliacoes) throws Exception {
		Double soma = 0.0;
		int qtdAvaliacoes = avaliacoes.size();
		
		if (qtdAvaliacoes > 0) {
			for (Avaliacao avaliacao : avaliacoes) {
				soma += avaliacao.getNota(); 
			}
			
			return (soma/qtdAvaliacoes);
		}
		
		return MEDIA_INICIAL;
	}
	
	public Set<Avaliacao> getAvaliacoes(Usuario usuario) {
		return usuario.getAvaliacoes();
	}

	public Double calcularAvaliacaoMedia(Usuario user) throws Exception {
		return calcularAvaliacaoMedia(user.getAvaliacoes());
	}

	public void avaliarUsuario(Usuario avaliador, AvaliacaoDTO avaliacao) throws Exception {
		if (avaliador.getTipo().equals(TipoUsuario.CLIENTE)) {
			clienteAvaliaFornecedor(avaliador, avaliacao);
		} else {
			fornecedorAvaliaCliente(avaliador, avaliacao);
		}
	}

	private void fornecedorAvaliaCliente(Usuario avaliador, AvaliacaoDTO avaliacao) throws Exception {
		Servico servicoAvaliado = servicoService.getServicoByID(avaliacao.getServico().getId());

		if (!servicoAvaliado.getFornecedor().getLogin().equalsIgnoreCase(avaliador.getLogin())) {
			throw new Exception("Só pode avaliar usuários que já prestaram serviços para você!");
		}

		if (servicoEhValidoParaAvaliar(servicoAvaliado, avaliador.getTipo())) {
			Usuario usuarioAvaliado = usuarioService.getByLogin(servicoAvaliado.getCliente().getLogin());
			
			Avaliacao avaliacaoCriada = addAvaliacao(avaliacao.getAvaliacao(), usuarioAvaliado, servicoAvaliado);
			usuarioAvaliado.addAvaliacao(avaliacaoCriada);
			usuarioService.atualizarUsuario(usuarioAvaliado);
			
			servicoAvaliado.setFornecedorAvaliou(true);
			servicoService.atualizarServico(servicoAvaliado);
		}
	}

	private void clienteAvaliaFornecedor(Usuario avaliador, AvaliacaoDTO avaliacao) throws Exception {
		Servico servicoAvaliado = servicoService.getServicoByID(avaliacao.getServico().getId());

		if (!servicoAvaliado.getCliente().getLogin().equalsIgnoreCase(avaliador.getLogin())) {
			throw new Exception("Só pode avaliar usuários que já prestaram serviços para você!");
		}

		if (servicoEhValidoParaAvaliar(servicoAvaliado, avaliador.getTipo())) {
			Usuario usuarioAvaliado = usuarioService.getByLogin(servicoAvaliado.getFornecedor().getLogin());
			
			Avaliacao avaliacaoCriada = addAvaliacao(avaliacao.getAvaliacao(), usuarioAvaliado, servicoAvaliado);
			usuarioAvaliado.addAvaliacao(avaliacaoCriada);
			usuarioService.atualizarUsuario(usuarioAvaliado);

			servicoAvaliado.setClienteAvaliou(true);
			servicoService.atualizarServico(servicoAvaliado);
		}
	}

	public boolean servicoEhValidoParaAvaliar(Servico servico, TipoUsuario avaliador) throws Exception {
		boolean servicoConcluido = servico.getStatus().equals(TipoStatus.CONCLUIDO);
		boolean usuarioJaAvaliou = servico.getQuemAvaliou().contains(avaliador);

		if (servicoConcluido && !usuarioJaAvaliou) {
			return true;

		} else if (servicoConcluido && usuarioJaAvaliou) {
			throw new Exception("Você já avaliou o usuário por esse serviço!");
		}

		throw new Exception("Você só pode avaliar o usuário, se o serviço estiver concluído!");
	}
}
