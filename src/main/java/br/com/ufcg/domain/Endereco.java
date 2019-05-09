package br.com.ufcg.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@Entity
@Table(name = "TAB_ENDERECO")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Endereco {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID_ENDERECO")
    private Long id;

    @Column(name = "TX_NOME_COMPLETO")
    private String rua;

    @Column(name = "TX_NUMERO")
    private String numero;

    @Column(name = "TX_BAIRRO")
    private String bairro;

    @Column(name = "CD_COMPLEMENTO", nullable = false)
    private String complemento;

    public Endereco() {}

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(rua, endereco.rua) &&
                Objects.equals(numero, endereco.numero) &&
                Objects.equals(bairro, endereco.bairro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rua, numero, bairro);
    }


}
