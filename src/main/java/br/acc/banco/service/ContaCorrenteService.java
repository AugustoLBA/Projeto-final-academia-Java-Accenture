package br.acc.banco.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.acc.banco.dto.ContaCorrenteCreateDTO;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.Cliente;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.repository.ContaCorrenteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class ContaCorrenteService {
	
	private final ContaCorrenteRepository contaCorrenteRepository;
	private final AgenciaService agenciaService;
	private final ClienteService clienteService;
	
	public ContaCorrente buscarPorId(Long id) {
		return contaCorrenteRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Conta com "+id+" não encontrada!"));
				
	}
	
	public ContaCorrente toContaCorrente(ContaCorrenteCreateDTO dto) {
		ContaCorrente contaCorrente = new ContaCorrente();
		BeanUtils.copyProperties(dto, ContaCorrente);
		contaCorrente.setAgencia(agenciaService.buscarPorId(dto.getAgenciaId()));
		contaCorrente.setCliente(clienteService.buscarPorId(dto.getClienteId()));
		return contaCorrente;
	}
	

}
