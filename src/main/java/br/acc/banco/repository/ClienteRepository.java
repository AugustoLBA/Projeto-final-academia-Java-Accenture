package br.acc.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.acc.banco.models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
