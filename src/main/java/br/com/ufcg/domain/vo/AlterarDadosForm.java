package br.com.ufcg.domain.vo;
import java.util.List;


import br.com.ufcg.domain.*;

public class AlterarDadosForm {
	
	private String novoNomeCompleto;
	private String novoEmail;
	private String novoLogin;
	private List<Especialidade> novaEspecialidades;
	private String novaFotoPerfil;
	
	public AlterarDadosForm() {
		
	}
	
	public AlterarDadosForm(String novoNomeCompleto, String novoLogin, String novoEmail, List<Especialidade> novaEspecialidades, String fotoPerfil) {
		this.novoNomeCompleto = novoNomeCompleto;
		this.novoLogin = novoLogin;
		this.novoEmail = novoEmail;
		this.novaEspecialidades = novaEspecialidades;
		this.setNovaFotoPerfil(fotoPerfil);
	}
	
	public String getNovoNomeCompleto() {
		return novoNomeCompleto;
	}
	public void setNovoNomeCompleto(String novoNomeCompleto) {
		this.novoNomeCompleto = novoNomeCompleto;
	}
	
	public String getNovoEmail() {
		return novoEmail;
	}
	public void setNovoEmail(String novoEmail) {
		this.novoEmail = novoEmail;
	}
	public String getNovoLogin() {
		return novoLogin;
	}
	public void setNovoLogin(String novoLogin) {
		this.novoLogin = novoLogin;
	}
	public List<Especialidade> getNovaEspecialidades() {
		return novaEspecialidades;
	}
	public void setNovaEspecialidades(List<Especialidade> novasEspecialidades) {
		this.novaEspecialidades = novasEspecialidades;
	}

	public String getNovaFotoPerfil() {
		return novaFotoPerfil;
	}

	public void setNovaFotoPerfil(String fotoPerfil) {
		this.novaFotoPerfil = fotoPerfil;
	}
	
}
