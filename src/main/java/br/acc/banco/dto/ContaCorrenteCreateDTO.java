package br.acc.banco.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContaCorrenteCreateDTO {

	@NotNull(message = "O numero da conta não pode se nulo !")
	private int numero;

	@NotNull(message = "O saldo da conta não pode ser nulo !")
	private BigDecimal saldo;

	@NotNull(message = "O ID da AGENCIA da conta não pode ser nulo !")
	private Long agenciaId;

	@NotNull(message = "O ID do CLIENTE da conta não pode ser nulo !")
	private Long clienteId;
}
