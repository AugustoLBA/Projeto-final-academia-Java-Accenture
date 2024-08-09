package br.acc.banco.dto.operacao;

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
