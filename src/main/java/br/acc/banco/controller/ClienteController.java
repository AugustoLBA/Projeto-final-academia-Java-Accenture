package br.acc.banco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.acc.banco.dto.ClienteCreateDTO;
import br.acc.banco.dto.ClienteResponseDTO;
import br.acc.banco.models.Cliente;
import br.acc.banco.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@RestController
@RequestMapping("api/banco/cliente")
public class ClienteController {
	
	private final ClienteService clienteService;
	
	@PostMapping
	public ResponseEntity<ClienteResponseDTO> save(@RequestBody @Valid ClienteCreateDTO createDTO){
		Cliente cliente = clienteService.salvar(clienteService.toCliente(createDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.toDto(cliente));
	}

}
