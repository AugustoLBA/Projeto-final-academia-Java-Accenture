package br.acc.banco.dto.emprestimo;

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
public class ParcelaEmprestimoDTO {

	@NotNull(message = "O valor da parcela não pode ser nulo !")
	private BigDecimal valor;
	
	@NotNull(message = "O ID da ContaCorrente não pode ser nulo !")
	private Long contaCorrenteId;
	
	@NotNull(message = "O ID do Emprestimo não pode ser nulo !")
	private Long emprestimoId;
}
