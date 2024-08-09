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
public class TransferenciaCreateDTO extends OperacaoCreateDTO {
	
	@NotNull(message = "O id da conta destino não pode ser vazio!")
	private Long contaCorrenteDestinoId;

}
