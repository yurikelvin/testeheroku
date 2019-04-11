package br.com.ufcg.projectes;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.ufcg.controllers.EspecialidadeController;
import br.com.ufcg.controllers.ServicoController;
import br.com.ufcg.controllers.UsuarioController;
import br.com.ufcg.domain.Cliente;
import br.com.ufcg.domain.Endereco;
import br.com.ufcg.domain.Especialidade;
import br.com.ufcg.domain.Fornecedor;
import br.com.ufcg.domain.Servico;
import br.com.ufcg.domain.Usuario;
import br.com.ufcg.domain.enums.TipoStatus;
import br.com.ufcg.repositories.EspecialidadeRepository;
import br.com.ufcg.repositories.ServicoRepository;
import br.com.ufcg.repositories.UsuarioRepository;
import br.com.ufcg.services.ServicoService;
import br.com.ufcg.services.UsuarioService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicoControllerTest {

	@Autowired
	ServicoController sc;
	
	@Autowired
	ServicoService ss;
	
	@Autowired
	UsuarioController uc;
	
	@Autowired
	EspecialidadeController ec;
	
	@Autowired
	UsuarioService us;
	
	@Autowired
	EspecialidadeRepository er;
	
	@Autowired
	UsuarioRepository ur;
	
	@Autowired
	ServicoRepository sr;
	
	private Especialidade especialidade1;
	private Especialidade especialidade2;
	private Especialidade especialidade3;
	private Usuario cliente1;
	private Usuario cliente2;
	private Usuario fornecedor1;
	private Usuario fornecedor2;
	private LocalDate data1;
	private LocalDate data2;
	private LocalTime time1;
	private LocalTime time2;
	private Endereco endereco1;
	private Endereco endereco2;
	private Servico servico1;
	private Servico servico2;
	
	
	@Before
	@Transactional
	public void setUp() {
		especialidade1 = new Especialidade("encanador");
		especialidade2 = new Especialidade("motorista");
		especialidade3 = new Especialidade("eletricista");
		
		List<Especialidade> listaEspecialidades1 = new ArrayList<>();
		List<Especialidade> listaEspecialidades2 = new ArrayList<>();
		
		listaEspecialidades1.add(especialidade1);
		listaEspecialidades1.add(especialidade3);
		
		listaEspecialidades2.add(especialidade1);
		listaEspecialidades2.add(especialidade2);
		listaEspecialidades2.add(especialidade3);
		
		cliente1 = new Cliente("Tiberio Gadelha M", "tiberiogadelha", "/imgs/foto.png",
				"tiberio.gomes@ccc.ufcg.edu.br", "123456789");
		cliente2 = new Cliente("Pedro Wanderley", "pedrow", "/imgs/foto.png",
				"pedro.wanderley@ccc.ufcg.edu.br", "abcdefghi");
		
		fornecedor1 = new Fornecedor("Carlos Alberto dos Anjos", "carlosaba", "/imgs/foto.png",
				"carlos.alberto1@gmail.com", "123456789", listaEspecialidades1);
		fornecedor2 = new Fornecedor("Jose Thiago Dos Santos", "jthiago", "/imgs/foto.png",
				"j.thiago@gmail.com", "123456789", listaEspecialidades2);
		
		try {
			ec.cadastraEspecialidade(especialidade1);
			ec.cadastraEspecialidade(especialidade2);
			ec.cadastraEspecialidade(especialidade3);
			
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		uc.cadastrarCliente((Cliente) cliente1);
		uc.cadastrarCliente((Cliente) cliente2);
		
		uc.cadastrarFornecedor((Fornecedor) fornecedor1);
		uc.cadastrarFornecedor((Fornecedor) fornecedor2);
		
		endereco1 = new Endereco();
		endereco1.setBairro("liberdade");
		endereco1.setRua("rua souza e lima");
		endereco1.setNumero("129");
		
		endereco2 = new Endereco();
		endereco2.setBairro("catole");
		endereco2.setNumero("456");
		endereco2.setRua("rua francisco");
		
		data1 = LocalDate.now().plusDays(40);
		data2 = LocalDate.now().plusDays(5);
		
		time1 = LocalTime.of(15, 0);
		time2 = LocalTime.of(12,30);
		
		BigDecimal valor1 = BigDecimal.valueOf(25.99);
		BigDecimal valor2 = BigDecimal.valueOf(359);
		
		servico1 = new Servico("encanador", "pia quebrada", data1, time1, valor1, endereco1);
		servico2 = new Servico("motorista", "uma viagem", data2, time2, valor2, endereco2);
		
		
	}
	
	@After
	@Transactional
	public void limpeza() {
		sr.deleteAll();
		ur.deleteAll();
		er.deleteAll();
		
	}
	
	
	@Test
	@Transactional
	public void testClienteCadastraServicoValido() throws Exception {
		Cliente cliente = (Cliente) us.getByLogin("pedrow");
		Servico servico1 = this.servico1;
		Servico servico2 = this.servico2;
		
		try {
			ss.criarServico(cliente, servico1);
			assertEquals(1, ss.getServicosCliente(cliente).size());
			ss.criarServico(cliente, servico2);
			assertEquals(2, ss.getServicosCliente(cliente).size());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@Transactional
	public void testClienteCadastraServicoDataInvalida() throws Exception {
		Usuario cliente = us.getByLogin(cliente2.getLogin());
		
		// Data de 200 dias atras
		LocalDate dataUltrapassada = LocalDate.now().minusDays(200);
		
		BigDecimal valor = BigDecimal.valueOf(6500);
		Servico servico = new Servico("encanador", "projeto de ponte", dataUltrapassada, time1, valor, endereco1);
		
		try {
			ss.criarServico(cliente, servico);
		} catch(Exception e) {
			assertEquals("Data do serviço não pode ser uma data passada.", e.getMessage());
		}
		
		LocalDate dataNula = null;
		
		Servico servico2 = new Servico("encanador", "projeto de ponte", dataNula, time1, valor, endereco1);
		
		try {
			ss.criarServico(cliente, servico2);
		} catch(Exception e) {
			assertEquals("A data do serviço deve ser informada.", e.getMessage());
		}
		
		// Data 5 anos a mais da data de hoje.
		LocalDate dataMuitoFutura = LocalDate.now().plusYears(5);
		Servico servico3 = new Servico("encanador", "projeto de ponte", dataMuitoFutura, time1, valor, endereco1);
		
		try {
			ss.criarServico(cliente, servico3);
		} catch(Exception e) {
			assertEquals("Data do serviço inválida, só pode cadastrar serviços com 3 meses de diferença.", e.getMessage());
		}
		
	}
	
	@Test
	@Transactional
	public void testClienteCadastraServicoHorarioInvalido() throws Exception {
		Usuario cliente = us.getByLogin(cliente1.getLogin());
		BigDecimal valor = BigDecimal.valueOf(25);
		LocalTime muitoCedo = LocalTime.of(5, 0);
		
		Servico servico1 = new Servico("motorista", "me levar ao hospital", data1, muitoCedo, valor, endereco1);
		
		try {
			ss.criarServico(cliente, servico1);
		} catch(Exception e) {
			assertEquals("Desculpe, só é permitido marcar serviços entre os horários de 7h - 20h", e.getMessage());
		}
		
		LocalTime muitoTarde = LocalTime.of(23, 50);
		Servico servico2 = new Servico("motorista", "me levar ao hospital", data1, muitoTarde, valor, endereco1);
		
		try {
			ss.criarServico(cliente, servico2);
		} catch(Exception e) {
			assertEquals("Desculpe, só é permitido marcar serviços entre os horários de 7h - 20h", e.getMessage());
		}
		
		LocalTime horaNula = null;
		Servico servico3 = new Servico("motorista", "me levar ao hospital", data1, horaNula, valor, endereco1);
		
		try {
			ss.criarServico(cliente, servico3);
		} catch(Exception e) {
			assertEquals("A hora do serviço deve ser informada.", e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testFornecedorVisualizaServicosDisponiveis() throws Exception {
		Usuario fornecedor = us.getByLogin("jthiago");
		Usuario cliente1 = us.getByLogin(this.cliente1.getLogin());
		Usuario cliente2 = us.getByLogin(this.cliente2.getLogin());
		
		/* Um servico que o fornecedor nao possui disponibilidade,
		 *  pois eh de uma especialidade que ele nao possui.
		 */
		Servico servico3 = new Servico("cozinheiro", "fazer um almoco", data1, time1, BigDecimal.valueOf(200), endereco2);
		
		Servico servicoCadastrado1 = ss.criarServico(cliente1, servico1);
		Servico servicoCadastrado2 = ss.criarServico(cliente2, servico2);
		Servico servicoCadastrado3 = ss.criarServico(cliente2, servico3);
		
		try {
			List<Servico> servicosDisponiveis = ss.getServicosDisponiveisFornecedor((Fornecedor) fornecedor);
			assertEquals(2, servicosDisponiveis.size());
			assertTrue(servicosDisponiveis.contains(servicoCadastrado1));
			assertTrue(servicosDisponiveis.contains(servicoCadastrado2));
			assertFalse(servicosDisponiveis.contains(servicoCadastrado3));
			
		} catch(Exception e) {
			assertEquals(null, e);
		}
	}
	
	@Test
	@Transactional
	public void testFornecedorAceitaServicoValido() throws Exception {
		Usuario fornecedor = us.getByLogin(fornecedor1.getLogin());
		
		Usuario cliente = us.getByLogin(this.cliente1.getLogin());
		Servico servicoCadastrado = ss.criarServico(cliente, servico1);
		
		assertEquals(1, ss.getServicosDisponiveisFornecedor((Fornecedor) fornecedor).size());
		Servico servicoAceito = ss.setServicoParaFornecedor(servicoCadastrado, (Fornecedor) fornecedor);
		
		// Apos aceitar o unico servico disponivel, nao resta nenhum outro para esse fornecedor.
		try {
			ss.getServicosDisponiveisFornecedor((Fornecedor) fornecedor).size();
		} catch(Exception e) {
			assertEquals("O usuário não possui nenhum serviço cadastrado!", e.getMessage());
		}
		
		// Verifica se o servico aceito esta nos servicos do fornecedor
		assertTrue(ss.getServicosDoFornecedor((Fornecedor) fornecedor).contains(servicoAceito));
		
	}
	
	@Test
	@Transactional
	public void testFornecedorTentaAceitarServicoInvalido() throws Exception {
		Usuario fornecedor = us.getByLogin(fornecedor2.getLogin());
		Usuario cliente = us.getByLogin(this.cliente2.getLogin());
		
		/* Um servico que o fornecedor nao possui disponibilidade,
		 *  pois eh de uma especialidade que ele nao possui.
		 */
		Servico servico = new Servico("cozinheiro", "fazer um almoco", data1, time1, BigDecimal.valueOf(200), endereco2);
		
		Servico servicoCadastrado = ss.criarServico(cliente, servico);
		
		// O fornecedor nao pode aceitar o servico porque ele nao possui a especialidade.
		try {
			ss.setServicoParaFornecedor(servicoCadastrado, fornecedor);
		} catch(Exception e) {
			assertEquals("Você não possui a especialidade requerida para o serviço", e.getMessage());
		}
		
		// O fornecedor nao pode aceitar o servico porque ele nao possui a especialidade.
		assertFalse(ss.servicoEhValidoParaFornecedor(servicoCadastrado, (Fornecedor) fornecedor));
		assertEquals(0, ss.getServicosDisponiveisFornecedor((Fornecedor) fornecedor).size());
	}
	
	@Test
	@Transactional
	public void testClienteTentaAceitarServico() throws Exception {
		Usuario cliente1 = us.getByLogin(this.cliente2.getLogin());
		Usuario cliente2 = us.getByLogin(this.cliente2.getLogin());
		
		Servico servicoCadastrado = ss.criarServico(cliente1, servico1);
		try {
			ss.setServicoParaFornecedor(servicoCadastrado, cliente2);
		} catch(Exception e) {
			assertEquals("Apenas fornecedores podem aceitar serviços!", e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testFornecedorConcluiServicoValido() throws Exception {
		Usuario fornecedor = us.getByLogin(fornecedor1.getLogin());
		
		Usuario cliente = us.getByLogin(this.cliente1.getLogin());
		Servico servicoCadastrado = ss.criarServico(cliente, servico1);
		assertEquals(1, ss.getServicosDisponiveisFornecedor((Fornecedor) fornecedor).size());
		Servico servicoAceito = ss.setServicoParaFornecedor(servicoCadastrado, fornecedor);
		assertEquals(0, ss.getServicosDisponiveisFornecedor((Fornecedor) fornecedor).size());
		assertTrue(ss.getServicosDoFornecedor((Fornecedor) fornecedor).contains(servicoAceito));
		Servico servicoConcluido = ss.concluirServico(servicoAceito, (Fornecedor) fornecedor);
		
		assertEquals(TipoStatus.CONCLUIDO, servicoConcluido.getStatus());
		
	}
	
	@Test
	@Transactional
	public void testFornecedorTentaConcluirServicoInvalido() throws Exception {
		Usuario fornecedor1 = us.getByLogin(this.fornecedor1.getLogin());
		Usuario fornecedor2 =  us.getByLogin(this.fornecedor2.getLogin());
		
		Usuario cliente = us.getByLogin(this.cliente1.getLogin());
		Servico servicoCadastrado = ss.criarServico(cliente, servico1);
		
		// O fornecedor 2 aceita o servico
		Servico servicoAceito = ss.setServicoParaFornecedor(servicoCadastrado, fornecedor2);
		
		// O fornecedor 1 tenta concluir o servico do fornecedor 2
		try {
			ss.concluirServico(servicoAceito, (Fornecedor) fornecedor1);
		} catch(Exception e) {
			assertEquals("Você só pode concluir serviços que você mesmo aceitou!", e.getMessage());
		}
		
		// O fornecedor 1 tenta concluir o servico do fornecedor 2
		assertFalse(ss.checarFornecedor(servicoAceito, (Fornecedor) fornecedor1));
		assertEquals(TipoStatus.ACEITO, servicoAceito.getStatus());
	}
	
	@Test
	@Transactional
	public void testFornecedorTentaConcluirServicoConcluido() throws Exception {
		Usuario fornecedor =  us.getByLogin(this.fornecedor2.getLogin());
		
		Usuario cliente = us.getByLogin(this.cliente1.getLogin());
		Servico servicoCadastrado = ss.criarServico(cliente, servico1);
		
		Servico servicoAceito = ss.setServicoParaFornecedor(servicoCadastrado, fornecedor);
		Servico servicoConcluido = ss.concluirServico(servicoAceito, (Fornecedor) fornecedor);
		
		// Fornecedor tenta concluir um servico que ja foi concluido
		try {
			ss.concluirServico(servicoConcluido, (Fornecedor) fornecedor);
		} catch(Exception e) {
			assertEquals("Só é possível concluir serviços que possuem status ACEITO!", e.getMessage());
		}
	}
	
	@Test
	@Transactional
	public void testClienteCancelaServicoValido() throws Exception {
		Usuario cliente = us.getByLogin(this.cliente1.getLogin());
		Servico servicoCadastrado = ss.criarServico(cliente, servico2);
		assertEquals(TipoStatus.EM_ABERTO, servicoCadastrado.getStatus());
		Servico servicoCancelado = ss.cancelarServicoCliente(servicoCadastrado, (Cliente) cliente);
		assertEquals(TipoStatus.CANCELADO, servicoCancelado.getStatus());
	}
	
	@Test
	@Transactional
	public void testClienteCancelaServicosInvalidos() throws Exception {
		Usuario cliente1 = us.getByLogin(this.cliente1.getLogin());
		Usuario cliente2 = us.getByLogin(this.cliente2.getLogin());
		Usuario fornecedor = us.getByLogin(this.fornecedor2.getLogin());
		
		Servico servicoCadastrado = ss.criarServico(cliente2, servico1);
		assertEquals(TipoStatus.EM_ABERTO, servicoCadastrado.getStatus());
		
		// Cliente tenta cancelar um servico que nao eh dele.
		try {
			ss.cancelarServicoCliente(servicoCadastrado, (Cliente) cliente1);
		} catch(Exception e) {
			assertEquals("Você só pode cancelar serviços que foram solicitados por você!", e.getMessage());
		}
		
		assertEquals(TipoStatus.EM_ABERTO, servicoCadastrado.getStatus());
		
		Servico servicoAceito = ss.setServicoParaFornecedor(servicoCadastrado, fornecedor);
		Servico servicoConcluido = ss.concluirServico(servicoAceito, (Fornecedor) fornecedor);
		assertEquals(TipoStatus.CONCLUIDO, servicoConcluido.getStatus());
		
		// Cliente tenta cancelar um servico que ja foi concluido
		try {
			ss.cancelarServicoCliente(servicoConcluido, (Cliente) cliente2);
		} catch(Exception e) {
			assertEquals("Não é possivel cancelar esse serviço, pois ele já foi cancelado ou concluido!", e.getMessage());
		}
		
		assertEquals(TipoStatus.CONCLUIDO, servicoConcluido.getStatus());
	}
	
	@Test
	@Transactional
	public void testFornecedorCancelaServicoValido() throws Exception {
		Usuario cliente1 = us.getByLogin(this.cliente1.getLogin());
		Usuario fornecedor = us.getByLogin(this.fornecedor2.getLogin());
		
		Servico servicoCadastrado = ss.criarServico(cliente1, servico1);
		assertEquals(TipoStatus.EM_ABERTO, servicoCadastrado.getStatus());
		
		Servico servicoAceito = ss.setServicoParaFornecedor(servicoCadastrado, fornecedor);
		assertEquals(TipoStatus.ACEITO, servicoAceito.getStatus());
		assertEquals(fornecedor, servicoAceito.getFornecedor());
		
		Servico servicoCanceladoPeloFornecedor = ss.cancelarServicoFornecedor(servicoAceito, (Fornecedor) fornecedor);
		
		assertEquals(null, servicoCanceladoPeloFornecedor.getFornecedor());
		assertEquals(TipoStatus.EM_ABERTO, servicoCanceladoPeloFornecedor.getStatus());
		
	}
	
	@Test
	@Transactional
	public void testFornecedorCancelaServicoInvalido() throws Exception {
		Usuario cliente = us.getByLogin(this.cliente1.getLogin());
		Usuario cliente2 = us.getByLogin(this.cliente2.getLogin());
		Usuario fornecedor1 = us.getByLogin(this.fornecedor1.getLogin());
		Usuario fornecedor2 = us.getByLogin(this.fornecedor2.getLogin());
		
		
		Servico servicoCadastrado = ss.criarServico(cliente, servico2);
		assertEquals(TipoStatus.EM_ABERTO, servicoCadastrado.getStatus());
		
		Servico servicoAceito = ss.setServicoParaFornecedor(servicoCadastrado, fornecedor2);
		assertEquals(TipoStatus.ACEITO, servicoAceito.getStatus());
		assertEquals(fornecedor2, servicoAceito.getFornecedor());
		
		
		Servico servicoCadastrado2 = ss.criarServico(cliente2, servico1);
		assertEquals(TipoStatus.EM_ABERTO, servicoCadastrado2.getStatus());
		
		Servico servicoAceito2 = ss.setServicoParaFornecedor(servicoCadastrado2, fornecedor1);
		assertEquals(TipoStatus.ACEITO, servicoAceito2.getStatus());
		assertEquals(fornecedor1, servicoAceito2.getFornecedor());
		
		// Um fornecedor tenta cancelar um servico que nao eh dele
		try {
			ss.cancelarServicoFornecedor(servicoAceito, (Fornecedor) fornecedor1);
		} catch(Exception e) {
			assertEquals("Você só pode cancelar serviços aceitos por você!", e.getMessage());
			assertEquals(fornecedor2, servicoAceito.getFornecedor());
		}
	}
	
	
}
