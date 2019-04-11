package br.com.ufcg.util.validadores;

import br.com.ufcg.domain.Servico;
import br.com.ufcg.services.EspecialidadeService;
import br.com.ufcg.util.UtilCampos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class ServicoValidador {


	
    private static final String VALOR_INVALIDO = "Valor do Serviço não pode ser menor ou igual a zero.";
    private static final String DATA_PASSADA = "Data do serviço não pode ser uma data passada.";
    private static final String DATA_INVALIDA = "Data do serviço inválida, só pode cadastrar serviços com 3 meses de diferença.";
    private static final String HORARIO_INVALIDO = "Desculpe, só é permitido marcar serviços entre os horários de 7h - 20h";

    private static final String HORA_OBRIGATORIA = "A hora do serviço deve ser informada.";
    private static final String DATA_OBRIGATORIA = "A data do serviço deve ser informada.";
	private static final String DESCRICAO_OBRIGATORIA = "Uma descrição do serviço é obrigatória";

	

    public static void valida(Servico servico) throws Exception {
        validaValor(servico.getValor());
        validaData(servico.getData());
        validaHorario(servico.getHorario());
        validaDescricao(servico.getDescricao());
     
        UtilCampos.validaTamanhoCampo(servico.getTipo(), 5, 20, "Tipo Serviço");
        UtilCampos.validaTamanhoCampo(servico.getEndereco().getBairro(), 2, 50, "Bairro");
        UtilCampos.validaTamanhoCampo(servico.getEndereco().getRua(), 2, 50, "Rua");
        UtilCampos.validaTamanhoCampo(servico.getEndereco().getNumero(), 0, 5, "Número da Residência");
        UtilCampos.validaTamanhoCampo(servico.getDescricao(), 8, 35, "Descrição do serviço");
    }

    private static void validaDescricao(String descricao) throws Exception {
		if(descricao == null) {
			throw new Exception(DESCRICAO_OBRIGATORIA);
		}
		
	}

	private static void validaData(LocalDate data) throws Exception {

        if(data == null) {
            throw new Exception(DATA_OBRIGATORIA);
        }

        LocalDate currentDate = LocalDate.now();
        if(data.isBefore(currentDate)) {
            throw new Exception(DATA_PASSADA);
        }

        LocalDate currentDateWith3Months = currentDate.plusMonths(3);

        if(data.isAfter(currentDateWith3Months)) {
            throw new Exception(DATA_INVALIDA);
        }
    }

    private static void validaHorario(LocalTime horario) throws Exception {
        if(horario == null) {
            throw new Exception(HORA_OBRIGATORIA);
        }
        int hour = horario.getHour();
        if(hour < 7 || hour > 20) {
            throw new Exception(HORARIO_INVALIDO);
        }
    }

    private static void validaValor(BigDecimal valor) throws Exception {
        if(valor.intValue() <= 0) {
            throw new Exception(VALOR_INVALIDO);
        }
    }
    

}
