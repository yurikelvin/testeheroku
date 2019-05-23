package br.com.ufcg.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name="TAB_OFERTA")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICO", unique = true, nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICO_ID", referencedColumnName = "ID_SERVICO")
    private Servico servico;

    @Column(name = "DC_DESCRICAO", nullable = false)
    private String descricao;

    @Column(name = "VL_VALOR", nullable = false)
    private BigDecimal valor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm")
    @Column(name = "HR_HORARIO", nullable = false)
    private LocalTime estimativaConclusao;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, fetch = FetchType.LAZY, targetEntity = Fornecedor.class)
    @JoinColumn(name = "FORNECEDOR", referencedColumnName = "ID_USUARIO")
    private Fornecedor fornecedor;

    public Oferta() {super();}

    public Oferta(String descricao, BigDecimal valor, LocalTime estimativaConclusao, Servico servico, Fornecedor fornecedor) {
        this.servico = servico;
        this.descricao = descricao;
        this.valor = valor;
        this.estimativaConclusao = estimativaConclusao;
        this.fornecedor = fornecedor;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalTime getEstimativaConclusao() {
        return estimativaConclusao;
    }

    public void setEstimativaConclusao(LocalTime estimativaConclusao) {
        this.estimativaConclusao = estimativaConclusao;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Oferta)) return false;
        Oferta oferta = (Oferta) o;
        return Objects.equals(getId(), oferta.getId()) &&
                Objects.equals(getServico(), oferta.getServico()) &&
                Objects.equals(getDescricao(), oferta.getDescricao()) &&
                Objects.equals(getValor(), oferta.getValor()) &&
                Objects.equals(getEstimativaConclusao(), oferta.getEstimativaConclusao());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getServico(), getDescricao(), getValor(), getEstimativaConclusao());
    }
}
