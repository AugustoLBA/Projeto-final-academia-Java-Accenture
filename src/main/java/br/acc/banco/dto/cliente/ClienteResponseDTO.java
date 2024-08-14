package br.acc.banco.dto.cliente;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClienteResponseDTO {
	
	private Long id;
	
	private String nome;

	
	private String cpf;

	private String telefone;
	
	private BigDecimal renda;
}
