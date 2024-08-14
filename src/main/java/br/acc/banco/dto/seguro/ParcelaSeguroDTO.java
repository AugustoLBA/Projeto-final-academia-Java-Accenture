package br.acc.banco.dto.seguro;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaSeguroDTO {

	@NotNull(message = "O valor da parcela não pode ser nulo !")
	private BigDecimal valor;
	
	@NotNull(message = "O ID da ContaCorrente não pode ser nulo !")
	private Long contaCorrenteId;
	
	@NotNull(message = "O ID do Seguro não pode ser nulo !")
	private Long seguroID;
}
