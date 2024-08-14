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

import br.acc.banco.dto.emprestimo.EmprestimoCreateDTO;
import br.acc.banco.dto.emprestimo.EmprestimoResponseDTO;
import br.acc.banco.mapper.EmprestimoMapper;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.service.EmprestimoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/banco/emprestimo")
public class EmprestimoController {

	private final EmprestimoService emprestimoService;
	private final EmprestimoMapper emprestimoMapper; 
	
	@PostMapping
	public ResponseEntity<EmprestimoResponseDTO> save(@Valid @RequestBody EmprestimoCreateDTO createDTO){
		Emprestimo emprestimo = emprestimoService.salvar(emprestimoMapper.toEmprestimo(createDTO));
		return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoMapper.toDto(emprestimo));
	}
	
	@GetMapping
	public ResponseEntity<List<EmprestimoResponseDTO>> getAll(){
		List<EmprestimoResponseDTO> dtos = emprestimoMapper.toListDto(emprestimoService.buscarTodos());
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmprestimoResponseDTO> getById(@PathVariable Long id){
		Emprestimo emprestimo = emprestimoService.buscarPorId(id);
		return ResponseEntity.status(HttpStatus.OK).body(emprestimoMapper.toDto(emprestimo));
	}
}
