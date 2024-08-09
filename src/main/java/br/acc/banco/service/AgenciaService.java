package br.acc.banco.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.UsernameUniqueViolationException;
import br.acc.banco.models.Agencia;
import br.acc.banco.repository.AgenciaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class AgenciaService {

	private final AgenciaRepository agenciaRepository;
	
	public Agencia salvar(Agencia agencia) {
		try {
		return agenciaRepository.save(agencia);
		}catch(DataIntegrityViolationException e) {
			throw new UsernameUniqueViolationException("Agencia com nome: "+agencia.getNome()
			+" já cadastrado!");
		}
	}
	
	public Agencia buscarPorId(Long id) {
		return agenciaRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Agência com "+id+" não encontrada!"));		
	}
	
	public List<Agencia> buscarTodos(){
		return agenciaRepository.findAll();
	}
	
	public void deletarPorId(Long id) {
		Agencia agencia = buscarPorId(id);
		agenciaRepository.delete(agencia);
	}
	
}
