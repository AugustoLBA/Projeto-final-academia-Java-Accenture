package br.acc.banco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.repository.EmprestimoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de dependência via lombok
@Service
public class EmprestimoService {

	private final EmprestimoRepository emprestimoRepository;
	
	@Transactional
	public Emprestimo salvar(Emprestimo emprestimo) {
		return emprestimoRepository.save(emprestimo);
	}
	
	
	public Emprestimo buscarPorId(Long id) {
		return emprestimoRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Emprestimo com "+id+" não encontrado!"));
	}
	
	
	public List<Emprestimo> buscarTodos(){
		return emprestimoRepository.findAll();
	}
	
	@Transactional
	public void deletarPorId(Long id) {
		Emprestimo emprestimo = buscarPorId(id);
		emprestimoRepository.delete(emprestimo);
	}
}
