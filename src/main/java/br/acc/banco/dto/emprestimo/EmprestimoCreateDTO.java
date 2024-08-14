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
public class EmprestimoCreateDTO {

	@NotNull(message = "O valor do emprestimo não pode ser nulo ")
	private BigDecimal valor;
	
	@NotNull(message = "A quantidade de parcelas não pode ser nula !")
	private int quantidadeParcelas;
	
	@NotNull(message = "O ID da ContaCorrente  não pode ser nulo !")
	private Long contaCorrenteId;

	
	
	
}
