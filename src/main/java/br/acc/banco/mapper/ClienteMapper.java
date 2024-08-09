package br.acc.banco.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.acc.banco.dto.cliente.ClienteCreateDTO;
import br.acc.banco.dto.cliente.ClienteResponseDTO;
import br.acc.banco.models.Cliente;

@Component
public class ClienteMapper {
	

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
