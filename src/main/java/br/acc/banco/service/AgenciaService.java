package br.acc.banco.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.acc.banco.dto.AgenciaCreateDTO;
import br.acc.banco.dto.AgenciaResponseDTO;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.models.Agencia;
import br.acc.banco.repository.AgenciaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class AgenciaService {

	private final AgenciaRepository agenciaRepository;
	
	public Agencia salvar(Agencia agencia) {
		return agenciaRepository.save(agencia);
	}
	
	public Agencia buscarPorId(Long id) {
		return agenciaRepository.findById(id).orElseThrow(
				()-> new EntityNotFoundException("Agência com "+id+" não encontrada!"));		
	}
	
	public List<Agencia> buscarTodos(){
		return agenciaRepository.findAll();
	}
	
	public void deletarPorId(Long id) {
		Agencia agencia = buscarPorId(id);
		agenciaRepository.delete(agencia);
	}
	
	public Agencia toAgencia(AgenciaCreateDTO createDTO) {
		Agencia agencia = new Agencia();
		BeanUtils.copyProperties(createDTO, agencia);
		return agencia;
	}
	
	public AgenciaResponseDTO toDto(Agencia agencia) {
		AgenciaResponseDTO responseDTO = new AgenciaResponseDTO();
		BeanUtils.copyProperties(agencia, responseDTO);
		return responseDTO;
	}
	
	public List<AgenciaResponseDTO> toListDto(List<Agencia> agencias){
		return agencias.stream().map(agencia -> toDto(agencia)).collect(Collectors.toList());
	}
}
