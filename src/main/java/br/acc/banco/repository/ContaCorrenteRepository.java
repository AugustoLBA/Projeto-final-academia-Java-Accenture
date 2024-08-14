package br.acc.banco.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.acc.banco.models.ContaCorrente;

public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long>{

	Optional<ContaCorrente> findByClienteId(Long id);

	Optional<ContaCorrente> findByChavePix(String chavePix);

	Optional<ContaCorrente> findByNumero(int numero);

}
