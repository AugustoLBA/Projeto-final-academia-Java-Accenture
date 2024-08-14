package br.acc.banco.dto.agencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgenciaCreateDTO {

	@NotBlank(message = "O nome da agencia não pode ser nulo !")
	private String nome;

	@NotBlank(message = "O telefone da agencia não pode ser nulo !")
	@Size(min = 11, max = 11)
	private String telefone;
	
	@NotBlank(message = "O endereco da agencia não pode ser nulo !")
	private String endereco;
}
