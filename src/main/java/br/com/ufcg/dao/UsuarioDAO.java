package br.com.ufcg.dao;

import br.com.ufcg.domain.enums.TipoUsuario;

public abstract class UsuarioDAO {
	private Long id;
	private String login;
	private String nomeCompleto;
	private String email;
	private String fotoPerfil;
	private TipoUsuario tipo;
	
	public UsuarioDAO(long id, String login, String nomeCompleto, String email, String fotoPerfil, TipoUsuario tipo) {
		this.id = id;
		this.login = login;
		this.nomeCompleto = nomeCompleto;
		this.email = email;
		this.fotoPerfil = fotoPerfil;
		this.tipo = tipo;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nome) {
		this.nomeCompleto = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipoUsuario) {
		this.tipo = tipoUsuario;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDAO other = (UsuarioDAO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario " + this.id + " - Login: " + this.login + ", Nome: " + this.nomeCompleto + ", Email: " + this.email + "." + System.lineSeparator();
	}
}
