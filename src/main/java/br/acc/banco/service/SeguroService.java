package br.acc.banco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.Seguro;
import br.acc.banco.repository.SeguroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeguroService {

	private final SeguroRepository seguroRepository;
	
	@Transactional
	public Seguro salvar(Seguro seguro) {
		return seguroRepository.save(seguro);
	}
	
	
	public Seguro buscarPorId(Long id) {
		return seguroRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("seguro com "+id+" não encontrado!"));
	}
	
	
	public List<Seguro> buscarTodos(){
		return seguroRepository.findAll();
	}
	
	@Transactional
	public void deletarPorId(Long id) {
		Seguro seguro = buscarPorId(id);
		seguroRepository.delete(seguro);
	}


	public List<Seguro> findByContaId(Long id) {
		return seguroRepository.findByContaId(id);
	}
}
