package br.acc.banco.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PixResponseDTO extends OperacaoResponseDTO{

	private String chavePix;
}
