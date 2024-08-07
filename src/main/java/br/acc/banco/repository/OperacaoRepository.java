package br.acc.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.acc.banco.models.Operacao;

public interface OperacaoRepository extends JpaRepository<Operacao, Long>{

}
