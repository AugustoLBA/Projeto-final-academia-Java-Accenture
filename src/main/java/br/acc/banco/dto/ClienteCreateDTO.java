package br.acc.banco.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteCreateDTO {


	@NotBlank(message = "O NOME do cliente não pode ser nulo !")
	private String nome;

	@NotBlank(message = "O CPF do cliente não pode ser nulo !")
	private String cpf;

	@NotBlank(message = "O TELEFONE do cliente não pode ser nulo !")
	private String telefone;
}
