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
	
	@NotNull(message = "O id da conta destino não pode ser vazio!")
	private Long contaCorrenteDestinoId;


	public TransferenciaCreateDTO(TipoOperacao tipo, BigDecimal valor, Long contaCorrenteId,
			@NotNull(message = "O id da conta destino não pode ser vazio!") Long contaCorrenteDestinoId) {
		super(tipo, valor, contaCorrenteId);
		this.contaCorrenteDestinoId = contaCorrenteDestinoId;
	}
	
	

}
