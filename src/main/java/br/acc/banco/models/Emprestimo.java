package br.acc.banco.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.acc.banco.models.enums.StatusEmprestimo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "emprestimos")
public class Emprestimo implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "valor",nullable = false)
	private BigDecimal valor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StatusEmprestimo status;
	
	@Column(name = "quatidade_parcelas", nullable = false)
	private int quantidadeParcelas;
	
	@Column(name = "quantidade_parcelas_pagas", nullable = false)
	private int quantidadeParcelasPagas;
	
	@Column(name = "valor_parcela", nullable = false)
	private BigDecimal valorParcela; 
	
	@ManyToOne
	@JoinColumn(name = "conta_corrente_id", nullable = false)
	private ContaCorrente conta;
	
	@Column(name = "data_criacao")
	private Date dataCriacao;
	
	@PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataCriacao = atual;
    }
}
