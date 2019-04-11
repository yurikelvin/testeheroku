package br.com.ufcg.dao;

import br.com.ufcg.domain.Servico;
import br.com.ufcg.domain.Usuario;

public class AvaliacaoDAO {

	private Long id;
	private Double nota;
	private ServicoDAO servico;
	private UsuarioDAO usuario;
	
	public AvaliacaoDAO(Long id, Double nota, Servico servico, Usuario usuario) {
		this.id = id;
		this.nota = nota;
		if(servico == null) {
			this.servico = null;
		} else {
			this.servico = servico.toDAO();
		}
		
		if(usuario == null) {
			this.usuario = null;
		} else {
			this.usuario = usuario.toDAO();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}

	public ServicoDAO getServico() {
		return servico;
	}

	public void setServico(ServicoDAO servico) {
		this.servico = servico;
	}

	public UsuarioDAO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDAO usuario) {
		this.usuario = usuario;
	}

}
