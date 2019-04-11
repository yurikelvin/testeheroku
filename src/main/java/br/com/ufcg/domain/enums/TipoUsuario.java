package br.com.ufcg.domain.enums;

public enum TipoUsuario {
	
	CLIENTE(0),
	FORNECEDOR(1);
	
	private final Integer tipo;
	
	TipoUsuario(Integer tipo) {
		this.tipo = tipo;
	}
	
	public Integer getTipo() {
		return this.tipo;
	}
}
