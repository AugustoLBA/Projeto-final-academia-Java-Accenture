package br.acc.banco.service;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import br.acc.banco.dto.ClienteCreateDTO;
import br.acc.banco.dto.ClienteResponseDTO;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.Cliente;
import br.acc.banco.models.Operacao;
import br.acc.banco.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;
	
	
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Cliente com "+id+" não encontrado!"));		
	}
	
	public Cliente salvar(Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	public Cliente toCliente(ClienteCreateDTO dto) {
		Cliente cliente = new Cliente();
		BeanUtils.copyProperties(dto, cliente);
		return cliente;
	}
	
	public ClienteResponseDTO toDto(Cliente cliente) {
		ClienteResponseDTO dto = new ClienteResponseDTO();
		BeanUtils.copyProperties(cliente, dto);
		return dto;
	}
}
