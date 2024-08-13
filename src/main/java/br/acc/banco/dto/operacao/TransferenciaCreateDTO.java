package br.acc.banco.dto.operacao;

import java.math.BigDecimal;

import br.acc.banco.models.enums.TipoOperacao;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TransferenciaCreateDTO extends OperacaoCreateDTO {
	
	@NotNull(message = "O numero da conta origem não pode ser vazio!")
	private int contaCorrenteOrigem;

	@NotNull(message = "O numero da conta destino não pode ser vazio!")
	private int contaCorrenteDestinoNumero;

	public TransferenciaCreateDTO(TipoOperacao tipo, BigDecimal valor, Long contaCorrenteId,
			@NotNull(message = "O numero da conta origem não pode ser vazio!") int contaCorrenteOrigem,
			@NotNull(message = "O numero da conta destino não pode ser vazio!") int contaCorrenteDestinoNumero) {
		super(tipo, valor, contaCorrenteId);
		this.contaCorrenteOrigem = contaCorrenteOrigem;
		this.contaCorrenteDestinoNumero = contaCorrenteDestinoNumero;
	}
	
	
	

}
