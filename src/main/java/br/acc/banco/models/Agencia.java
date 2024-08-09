package br.acc.banco.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "agencias")
public class Agencia implements Serializable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;
	
	@Column(name = "nome", nullable = false,length = 100, unique = true)
	private String nome;
	
	@Column(name = "telefone", nullable = false,length = 11, unique = true)
	private String telefone;
	
	@Column(name = "endereco", unique = true)
	private String endereco;
	
	@OneToMany(mappedBy = "agencia")
	private Set<ContaCorrente> contas = new HashSet();
}
