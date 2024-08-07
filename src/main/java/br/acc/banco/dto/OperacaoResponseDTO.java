package br.acc.banco.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.acc.banco.models.Operacao;
import lombok.Data;

@Data
public class OperacaoResponseDTO {

	private Long id;

    private LocalDateTime dataRealizada;

    private Operacao.TipoOperacao tipo;

    private BigDecimal valor;

    private Long contaCorrenteId;
}
