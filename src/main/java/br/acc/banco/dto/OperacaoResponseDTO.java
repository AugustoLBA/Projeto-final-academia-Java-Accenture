package br.acc.banco.dto;

import java.math.BigDecimal;
import java.util.Date;

import br.acc.banco.models.enums.TipoOperacao;
import lombok.Data;

@Data
public class OperacaoResponseDTO {

	private Long id;

    private Date dataRealizada;

    private TipoOperacao tipo;

    private BigDecimal valor;

    private Long contaCorrenteId;
}
