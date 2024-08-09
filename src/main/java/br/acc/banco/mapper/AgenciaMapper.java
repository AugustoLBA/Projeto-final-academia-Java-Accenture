package br.acc.banco.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.acc.banco.dto.AgenciaCreateDTO;
import br.acc.banco.dto.AgenciaResponseDTO;
import br.acc.banco.models.Agencia;

@Component
public class AgenciaMapper {

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
