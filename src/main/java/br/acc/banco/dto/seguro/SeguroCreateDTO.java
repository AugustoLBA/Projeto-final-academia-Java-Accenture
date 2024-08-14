package br.acc.banco.dto.seguro;

import java.math.BigDecimal;

import br.acc.banco.models.enums.TipoSeguro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeguroCreateDTO {

	@NotNull(message = "O valor do seguro não pode ser nulo ")
	private BigDecimal valor;
	
	@NotNull(message = "A quantidade de parcelas não pode ser nula !")
	private int quantidadeParcelas;
	
	@NotNull(message = "O ID da ContaCorrente  não pode ser nulo !")
	private Long contaCorrenteId;
	
	@NotNull(message = "O tipo do seguro não pode ser nulo")
	private TipoSeguro tipo;

	public SeguroCreateDTO(@NotNull(message = "O valor do seguro não pode ser nulo ") BigDecimal valor,
			@NotNull(message = "A quantidade de parcelas não pode ser nula !") int quantidadeParcelas,
			@NotNull(message = "O tipo do seguro não pode ser nulo") TipoSeguro tipo,
			@NotNull(message = "O ID da ContaCorrente  não pode ser nulo !") Long contaCorrenteId) {
		super();
		this.valor = valor;
		this.quantidadeParcelas = quantidadeParcelas;
		this.contaCorrenteId = contaCorrenteId;
		this.tipo = tipo;
	}
	
	
}
