package br.acc.banco.service;

import java.util.List;
import java.util.stream.Collectors;

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

	public Cliente salvar(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Cliente com " + id + " não encontrado!"));
	}
	
	public List<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}
	
	public void deletarPorId(Long id) {
		Cliente cliente = buscarPorId(id);
		clienteRepository.delete(cliente);
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

	public List<ClienteResponseDTO> toListDto(List<Cliente> clientes) {
		return clientes.stream().map(cliente -> toDto(cliente)).collect(Collectors.toList());
	}
}
