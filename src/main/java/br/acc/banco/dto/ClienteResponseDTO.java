package br.acc.banco.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ClienteResponseDTO {
	
	private Long id;
	
	private String nome;

	
	private String cpf;

	
	private String telefone;
}
