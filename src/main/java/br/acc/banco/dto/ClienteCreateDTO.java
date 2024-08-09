package br.acc.banco.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClienteCreateDTO {


	@NotBlank(message = "O NOME do cliente não pode ser nulo !")
	private String nome;

	@NotBlank(message = "O CPF do cliente não pode ser nulo !")
	private String cpf;

	@NotBlank(message = "O TELEFONE do cliente não pode ser nulo !")
	private String telefone;
	
	@NotNull(message = "A renda do cliente não pode ser nulo!")
	private BigDecimal renda;
}
