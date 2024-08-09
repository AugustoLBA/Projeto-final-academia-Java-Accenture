package br.acc.banco.dto.operacao;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompraCreateDTO extends OperacaoCreateDTO{

	@NotNull(message = "O nome do estabelecimento não pode ser vazio!")
	private String nomeEstabelecimento;
}
