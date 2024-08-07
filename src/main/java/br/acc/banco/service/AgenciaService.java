package br.acc.banco.service;

import org.springframework.stereotype.Service;

import br.acc.banco.repository.AgenciaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class AgenciaService {

	private final AgenciaRepository agenciaRepository;
}
