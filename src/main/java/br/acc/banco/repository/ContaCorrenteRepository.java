package br.acc.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.acc.banco.models.ContaCorrente;

public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long>{

}
