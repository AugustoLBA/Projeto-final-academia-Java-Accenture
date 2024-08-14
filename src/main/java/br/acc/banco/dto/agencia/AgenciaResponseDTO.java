package br.acc.banco.dto.agencia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AgenciaResponseDTO {

	  private Long id;

	  private String nome;

	  private String telefone;
	  
	  private String endereco;
}
