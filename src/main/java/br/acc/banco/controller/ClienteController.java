package br.acc.banco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.acc.banco.dto.cliente.ClienteCreateDTO;
import br.acc.banco.dto.cliente.ClienteResponseDTO;
import br.acc.banco.mapper.ClienteMapper;
import br.acc.banco.models.Cliente;
import br.acc.banco.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@RestController
@RequestMapping("api/banco/cliente")
public class ClienteController {
	
	private final ClienteService clienteService;
	
	private final ClienteMapper clienteMapper;
	
	@PostMapping
	public ResponseEntity<ClienteResponseDTO> save(@RequestBody @Valid ClienteCreateDTO createDTO){
		Cliente cliente = clienteService.salvar(clienteMapper.toCliente(createDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toDto(cliente));
	}
	
	@GetMapping
	public ResponseEntity<List<ClienteResponseDTO>> findAll(){
		List<Cliente> clientes = clienteService.buscarTodos();
		return ResponseEntity.status(HttpStatus.OK).body(clienteMapper.toListDto(clientes));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> findById(@PathVariable Long id){
		Cliente cliente = clienteService.buscarPorId(id);
		return ResponseEntity.status(HttpStatus.OK).body(clienteMapper.toDto(cliente));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		clienteService.deletarPorId(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
