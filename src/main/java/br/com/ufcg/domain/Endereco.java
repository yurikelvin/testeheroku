package br.com.ufcg.domain;

import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "TAB_ENDERECO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Endereco {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID_ENDERECO")
    private Long id;
 
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID_LOCATION")
    public Location location;
    
    @Column(name = "TX_NOME", nullable = false)
    private String nome;

    @Column(name = "TX_PONTOREFERENCIA", nullable = false)
    private String pontoReferencia;
   

    public Endereco() {}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public String getPontoReferencia() {
		return pontoReferencia;
	}


	public void setPontoReferencia(String pontoReferencia) {
		this.pontoReferencia = pontoReferencia;
	}
	
	public String getNome() { 
		return this.nome; 
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((pontoReferencia == null) ? 0 : pontoReferencia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) { 
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		
        Endereco endereco = (Endereco) obj;
        
        return Objects.equals(nome, endereco.nome) &&
                Objects.equals(location, endereco.location) &&
                Objects.equals(pontoReferencia, endereco.pontoReferencia);
	}
}
