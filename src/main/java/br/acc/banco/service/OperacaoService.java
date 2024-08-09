package br.acc.banco.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.acc.banco.dto.OperacaoCreateDTO;
import br.acc.banco.dto.OperacaoResponseDTO;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Operacao;
import br.acc.banco.repository.ContaCorrenteRepository;
import br.acc.banco.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class OperacaoService {
	
	private final OperacaoRepository operacaoRepository;
	
	private final ContaCorrenteRepository contaCorrenteRepository;
	
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
	
	public ContaCorrente buscarPorIdConta(Long id) {
		return contaCorrenteRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Conta com "+id+" não encontrada!"));

	}
	
	public Operacao toOperacao(OperacaoCreateDTO dto) {
		Operacao operacao = new Operacao();
		BeanUtils.copyProperties(dto, operacao);
		operacao.setConta(buscarPorIdConta(dto.getContaCorrenteId()));
		return operacao;
	}

	public OperacaoResponseDTO toDto(Operacao operacao) {
		OperacaoResponseDTO responseDTO = new OperacaoResponseDTO();
		BeanUtils.copyProperties(operacao, responseDTO);
		responseDTO.setContaCorrenteId(operacao.getConta().getId());
		return responseDTO;
	}

	public List<OperacaoResponseDTO> toListDto(List<Operacao> operacoes){
		return operacoes.stream().map(responseDto -> toDto(responseDto)).collect(Collectors.toList());
	}
	
	

}
