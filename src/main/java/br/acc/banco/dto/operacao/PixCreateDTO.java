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
public class PixCreateDTO extends OperacaoCreateDTO {

	@NotNull(message = "A chave pix origem não pode ser nula!")
	private String chavePixOrigem;
	
	@NotNull(message = "A chave pix destino não pode ser nula!")
	private String chavePixDestino;

	public PixCreateDTO(TipoOperacao tipo, BigDecimal valor, Long contaCorrenteId,
			@NotNull(message = "A chave pix origem não pode ser nula!") String chavePixOrigem,
			@NotNull(message = "A chave pix destino não pode ser nula!") String chavePixDestino) {
		super(tipo, valor, contaCorrenteId);
		this.chavePixOrigem = chavePixOrigem;
		this.chavePixDestino = chavePixDestino;
	}


}
