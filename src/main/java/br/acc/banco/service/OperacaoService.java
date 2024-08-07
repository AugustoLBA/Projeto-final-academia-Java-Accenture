package br.acc.banco.service;

import org.springframework.stereotype.Service;

import br.acc.banco.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class OperacaoService {
	
	private final OperacaoRepository operacaoRepository;

}
