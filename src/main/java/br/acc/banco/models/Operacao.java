package br.acc.banco.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.acc.banco.models.enums.TipoOperacao;
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
@Table(name = "operacoes")
public class Operacao implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	
	@Column(name = "data_realizada")
	private Date dataRealizada;
	
	@Column(name = "valor", nullable = false)
	private BigDecimal valor;
	
	@ManyToOne
	@JoinColumn(name = "conta_corrente_id")
	private ContaCorrente conta;
	
	@Column(name = "nome_estabeleciento", nullable = true)
	private String nomeEstabelecimento;
	
	@Column(name = "chave_pix", nullable = true)
	private String chavePix;
	
	@ManyToOne
	@JoinColumn(name = "conta_corrente_destino_id", nullable = true)
	private ContaCorrente contaDestino;
	
	@ManyToOne
	@JoinColumn(name = "emprestimo_id", nullable = true)
	private Emprestimo emprestimo;
	
	@ManyToOne
	@JoinColumn(name = "seguro", nullable = true)
	private Seguro seguro;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo", nullable = false, length = 20)
	private TipoOperacao tipo;
	
	@PrePersist
    public void prePersist() {
        final Date atual = new Date();
        dataRealizada = atual;
    }
}
