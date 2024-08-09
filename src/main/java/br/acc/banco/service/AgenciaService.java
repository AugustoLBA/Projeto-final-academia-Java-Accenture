package br.acc.banco.service;

import org.springframework.stereotype.Service;

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.Agencia;
import br.acc.banco.repository.AgenciaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class AgenciaService {

	private final AgenciaRepository agenciaRepository;
	
	public Agencia buscarPorId(Long id) {
		return agenciaRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Agência com "+id+" não encontrada!"));		
	}
}
