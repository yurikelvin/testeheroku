package br.com.ufcg.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.ufcg.dao.ServicoDAO;
import br.com.ufcg.domain.enums.TipoStatus;
import br.com.ufcg.domain.enums.TipoUsuario;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "TAB_SERVICO")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Servico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SERVICO", unique = true, nullable = false)
	private Long id;

	@Column(name = "CD_TIPO", nullable = false)
	private String tipo;

	@Column(name = "DC_DESCRICAO", nullable = false)
	private String descricao;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "DT_DATA", nullable = false)
	private LocalDate data;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm")
	@Column(name = "HR_HORARIO", nullable = false)
	private LocalTime horario;

	@Column(name = "VL_VALOR", nullable = false)
	private BigDecimal valor;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ED_ENDERECO", referencedColumnName = "ID_ENDERECO", updatable = false)
	private Endereco endereco;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, fetch = FetchType.LAZY, targetEntity = Cliente.class)
	@JoinColumn(name = "CLIENTE", referencedColumnName = "ID_USUARIO", updatable = false)
	private Cliente cliente;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, fetch = FetchType.LAZY, targetEntity = Fornecedor.class)
	@JoinColumn(name = "FORNECEDOR", referencedColumnName = "ID_USUARIO")
	private Fornecedor fornecedor;

	@Enumerated
	@Column(name = "CD_STATUS")
	private TipoStatus status;
	
	@Column(name = "cliente_avaliou")
	private boolean clienteAvaliou;

	@Column(name = "fornecedor_avaliou")
	private boolean fornecedorAvaliou;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servico", fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
	private List<Oferta> ofertasRecebidas;

	public Servico() {
	    super();
        this.clienteAvaliou = false;
        this.fornecedorAvaliou = false;
        this.ofertasRecebidas = new ArrayList<>();
	}

	public Servico(String tipo, String descricao, LocalDate data, LocalTime horario, BigDecimal valor,
			Endereco endereco) {
		super();
		this.tipo = tipo;
		this.descricao = descricao;
		this.data = data;
		this.horario = horario;
		this.valor = valor;
		this.endereco = endereco;
		this.clienteAvaliou = false;
		this.fornecedorAvaliou = false;
		this.ofertasRecebidas = new ArrayList<>();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public TipoStatus getStatus() {
		return status;
	}

	public void setStatus(TipoStatus status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isClienteAvaliou() {
		return clienteAvaliou;
	}

	public void setClienteAvaliou(boolean clienteAvaliou) {
		this.clienteAvaliou = clienteAvaliou;
	}

	public boolean isFornecedorAvaliou() {
		return fornecedorAvaliou;
	}

	public void setFornecedorAvaliou(boolean fornecedorAvaliou) {
		this.fornecedorAvaliou = fornecedorAvaliou;
	}

    public Oferta getOfertaFinal() {
	    if(this.status.equals(TipoStatus.AGUARDANDO_OFERTAS) || this.status.equals(TipoStatus.CANCELADO)){
            return null;
        }

	    Oferta ofertaFinal = null;
	    boolean achou = false;
		Iterator<Oferta> iterator =  this.ofertasRecebidas.iterator();

		while(iterator.hasNext() && !achou) {
			Oferta oferta = iterator.next();
			if(oferta.getFornecedor().getId().equals(fornecedor.getId())) {
				ofertaFinal = oferta;
				achou = true;
			}
		}

        return ofertaFinal;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Servico servico = (Servico) o;
		return Objects.equals(data, servico.data) && Objects.equals(horario, servico.horario)
				&& Objects.equals(valor, servico.valor) && Objects.equals(endereco, servico.endereco)
				&& Objects.equals(tipo, servico.tipo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(data, horario, valor, endereco, tipo);
	}

	public ServicoDAO toDAO() {
		return new ServicoDAO(this.id, this.tipo, this.descricao, this.data, this.horario, this.valor, this.endereco,
				this.cliente, this.fornecedor, this.status, this.clienteAvaliou, this.fornecedorAvaliou, this.ofertasRecebidas, this.getOfertaFinal());
	}

	public List<TipoUsuario> getQuemAvaliou() {
		List<TipoUsuario> usuariosQueAvaliaram = new ArrayList<>();

		if (clienteAvaliou) {
			usuariosQueAvaliaram.add(TipoUsuario.CLIENTE);
		}

		if (fornecedorAvaliou) {
			usuariosQueAvaliaram.add(TipoUsuario.FORNECEDOR);
		}

		return usuariosQueAvaliaram;
	}

	public boolean adicionaOferta(Oferta oferta) {
	    return this.ofertasRecebidas.add(oferta);
    }

    public List<Oferta> getOfertasRecebidas() {
        return ofertasRecebidas;
    }

    public void setOfertasRecebidas(List<Oferta> ofertasRecebidas) {
        this.ofertasRecebidas = ofertasRecebidas;
    }
}
