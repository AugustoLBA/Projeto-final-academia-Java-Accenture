package br.acc.banco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.acc.banco.models.Seguro;

@Repository
public interface SeguroRepository extends JpaRepository<Seguro, Long>{

}
