package br.acc.banco.dto.operacao;

import java.math.BigDecimal;

import br.acc.banco.models.enums.TipoOperacao;
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

	public CompraCreateDTO(TipoOperacao tipo, BigDecimal valor, Long contaCorrenteId,
			@NotNull(message = "O nome do estabelecimento não pode ser vazio!") String nomeEstabelecimento) {
		super(tipo, valor, contaCorrenteId);
		this.nomeEstabelecimento = nomeEstabelecimento;
	}

	

	
	
	
	
}
