package br.acc.banco.models;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nome", nullable = false, length = 100)
	private String nome;
	
	@Column(name = "cpf", nullable = false, unique = true, length = 11)
	private String cpf;

	@Column(name = "telefone", nullable = false, unique = true, length = 11)
	private String telefone;

	@Column(name = "renda", nullable = false)
	private BigDecimal renda;

	@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
	private ContaCorrente contaCorrente;

}
