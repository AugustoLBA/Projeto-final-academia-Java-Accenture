package br.acc.banco.dto.emprestimo;

import java.math.BigDecimal;

import br.acc.banco.models.enums.StatusEmprestimo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoResponseDTO {

	private Long id;
	
	private BigDecimal valor;
	
	private StatusEmprestimo status;
	
	private int quatidadeParcelas;
	
	private int quantidadesParcelasPagas;
	
	private BigDecimal valorparcela;
	
}
