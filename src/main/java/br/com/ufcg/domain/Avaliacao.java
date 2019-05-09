package br.com.ufcg.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.ufcg.dao.AvaliacaoDAO;

@Entity
@Table(name="TAB_AVALIACAO")
public class Avaliacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ID_AVALIACAO")
    private Long id;

    @Column(name = "VL_NOTA")
    private Double nota;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICO_ID", referencedColumnName = "ID_SERVICO")
    private Servico servico;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID_USUARIO", updatable = false)
    private Usuario usuario;

    public Avaliacao() {
        super();
    }

    public Avaliacao(Double nota) {
        super();
        this.nota = nota;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNota() {
        return this.nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public AvaliacaoDAO toDAO() {
        return new AvaliacaoDAO(this.id, this.nota, this.servico, this.usuario);
    }
}