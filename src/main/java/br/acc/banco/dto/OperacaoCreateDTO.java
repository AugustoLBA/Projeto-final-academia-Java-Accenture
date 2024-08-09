package br.acc.banco.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.acc.banco.models.enums.TipoOperacao;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OperacaoCreateDTO {
	
	@NotNull(message = "O tipo de operação não pode se nulo !")
    private TipoOperacao tipo;

	@NotNull(message = "O valor da operação não pode se nulo !")
    private BigDecimal valor;

	@NotNull(message = "O ID da CONTA da conta não pode ser nulo !")
    private Long contaCorrenteId;

}
