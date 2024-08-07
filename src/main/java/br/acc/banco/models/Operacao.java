package br.acc.banco.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "operacoes")
public class Operacao implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
	@Column(name = "data_realizada")
	private LocalDateTime dataRealizada;
	
	@Column(name = "valor", nullable = false)
	private BigDecimal valor;
	
	
	@ManyToOne
	@JoinColumn(name = "conta_corrente_id")
	private ContaCorrente conta;
	
	@Column(name = "nome_estabeleciento", nullable = true)
	private String nomeEstabelecimento;
	
	@ManyToOne
	@JoinColumn(name = "conta_corrente_destino_id", nullable = true)
	private ContaCorrente contaDestino;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false)
	private TipoOperacao tipo;
	
	
	public enum TipoOperacao{
		SAQUE,DEPOSITO,TRANSFERENCIA,COMPRA,PIX;
	}
}
