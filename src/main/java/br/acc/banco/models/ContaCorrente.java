package br.acc.banco.models;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "contasCorrentes")
public class ContaCorrente implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;
	
	@Column(name = "numero", nullable = false, unique = true)
    private int numero;
	
	@Column(name = "saldo", nullable = false)
    private BigDecimal saldo;
	
	@ManyToOne
    @JoinColumn(name = "agencia_id")
    private Agencia agencia;
	
	@OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
	
	@OneToMany(mappedBy = "conta")
	private List<Operacao> operacoes = new ArrayList<>();
	
	
}
