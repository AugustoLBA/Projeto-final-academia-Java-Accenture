package br.acc.banco.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.acc.banco.dto.seguro.SeguroCreateDTO;
import br.acc.banco.dto.seguro.SeguroResponseDTO;
import br.acc.banco.mapper.SeguroMapper;
import br.acc.banco.models.Seguro;
import br.acc.banco.service.SeguroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/banco/seguro")
public class SeguroController {

	private final SeguroService seguroService;
	private final SeguroMapper seguroMapper;

	@PostMapping
	public ResponseEntity<SeguroResponseDTO> save(@Valid @RequestBody SeguroCreateDTO createDTO) {
		Seguro seguro = seguroService.salvar(seguroMapper.toSeguro(createDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(seguroMapper.toDto(seguro));
	}

	@GetMapping
	public ResponseEntity<List<SeguroResponseDTO>> getAll() {
		List<SeguroResponseDTO> dtos = seguroMapper.toListDto(seguroService.buscarTodos());
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SeguroResponseDTO> getById(@PathVariable Long id) {
		Seguro seguro = seguroService.buscarPorId(id);
		return ResponseEntity.status(HttpStatus.OK).body(seguroMapper.toDto(seguro));
	}
}
