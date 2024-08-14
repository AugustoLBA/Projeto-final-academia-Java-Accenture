package br.acc.banco.service;

import java.util.List;
import org.springframework.stereotype.Service;

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.Operacao;
import br.acc.banco.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class OperacaoService {
	
	private final OperacaoRepository operacaoRepository;
	
	public Operacao salvar(Operacao operacao) {
			return operacaoRepository.save(operacao);
		
	}
	
	public List<Operacao> buscarTodas(){
		return operacaoRepository.findAll();
	}
	
	public Operacao buscarPorId(Long id) {
		return operacaoRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Operação com "+id+" não encontrada!"));		
	}
	
	public void deletarPorId(Long id) {
		Operacao operacao = buscarPorId(id);
		operacaoRepository.delete(operacao);
	}
	
}
