package br.acc.banco.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ClienteResponseDTO {
	
	private Long id;
	
	private String nome;

	
	private String cpf;

	private String telefone;
	
	private BigDecimal renda;
}
