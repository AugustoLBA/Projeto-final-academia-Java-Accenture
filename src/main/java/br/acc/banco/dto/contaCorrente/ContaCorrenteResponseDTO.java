package br.acc.banco.dto.contaCorrente;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaCorrenteResponseDTO {
	
	 	private Long id;
	 	
	    private int numero;

	    private BigDecimal saldo;

	    private Long agenciaId;

	    private Long clienteId;
	    
	    private Date dataCriacao;
	    
	    private String chavePix;
}
