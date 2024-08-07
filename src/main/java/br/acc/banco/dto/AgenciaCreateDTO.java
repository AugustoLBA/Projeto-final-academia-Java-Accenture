package br.acc.banco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AgenciaCreateDTO {

	@NotBlank(message = "O nome da agencia não pode ser nulo !")
	private String nome;

	@NotNull(message = "O numero da agencia não pode ser nulo !")
	private int numero;

	@NotBlank(message = "O telefone da agencia não pode ser nulo !")
	@Size(min = 11, max = 11)
	private String telefone;
}
