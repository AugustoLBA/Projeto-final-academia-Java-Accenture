package br.acc.banco.dto;

import java.math.BigDecimal;

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

	    private String senha;

	    private Long agenciaId;

	    private Long clienteId;
}
