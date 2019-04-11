package br.com.ufcg.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ufcg.dao.ClienteDAO;
import br.com.ufcg.dao.UsuarioDAO;
import br.com.ufcg.domain.enums.TipoUsuario;

@Entity
@DiscriminatorValue(value = "Cliente")
@Embeddable
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cliente extends Usuario {

	private static final long serialVersionUID = 1L;

	public Cliente(String nomeCompleto, String login, String fotoPerfil, 
			String email, String senha) {
		super(nomeCompleto, login, fotoPerfil, email, senha, TipoUsuario.CLIENTE);
	}
	
	public Cliente() {
		super(null, null, null, null, null, TipoUsuario.CLIENTE);
	}
	
	@Override
	public String toString() {
		return "Cliente " + super.getId() + ": nome - " + super.getNomeCompleto();
	}

	@Override
	public UsuarioDAO toDAO() {
		return new ClienteDAO(this.getId(), this.getLogin(), this.getNomeCompleto(), this.getEmail(), this.getFotoPerfil(), this.getTipo());
	}
}
