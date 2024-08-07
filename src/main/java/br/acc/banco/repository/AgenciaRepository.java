package br.acc.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.acc.banco.models.Agencia;

public interface AgenciaRepository extends JpaRepository<Agencia, Long>{

}
