package br.acc.banco.dto.operacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaSeguroDTO extends OperacaoResponseDTO {

	private Long seguroId;
}
