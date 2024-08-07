package br.acc.banco.service;

import org.springframework.stereotype.Service;

import br.acc.banco.repository.ContaCorrenteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class ContaCorrenteService {
	
	private final ContaCorrenteRepository contaCorrenteRepository;

}
