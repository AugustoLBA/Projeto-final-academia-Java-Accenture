package br.acc.banco.dto.operacao;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaResponseDTO extends OperacaoResponseDTO{
	
	private int contaCorrenteOrigem;
	
	private int contaCorrenteDestinoNumero;
}
