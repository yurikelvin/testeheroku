package br.com.ufcg.dto;

import br.com.ufcg.domain.Oferta;
import br.com.ufcg.domain.Servico;

public class ServicoDTO {
	
	private Oferta oferta;
	private Servico servico;
	
	public ServicoDTO() {}
	
	public ServicoDTO(Oferta oferta, Servico servico) {
		this.servico = servico;
		this.oferta = oferta;
	}
	
	public Oferta getOferta() { 
		return this.oferta;
	}
	
	public Servico getServico() {
		return this.servico;
	}

}
