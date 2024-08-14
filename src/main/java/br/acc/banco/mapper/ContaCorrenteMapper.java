package br.acc.banco.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.acc.banco.dto.contaCorrente.ContaCorrenteCreateDTO;
import br.acc.banco.dto.contaCorrente.ContaCorrenteResponseDTO;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.service.AgenciaService;
import br.acc.banco.service.ClienteService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContaCorrenteMapper {

	private final AgenciaService agenciaService;
	private final ClienteService clienteService;
	
	
	public ContaCorrente toContaCorrente(ContaCorrenteCreateDTO dto) {
		ContaCorrente contaCorrente = new ContaCorrente();
		BeanUtils.copyProperties(dto, contaCorrente);
		contaCorrente.setAgencia(agenciaService.buscarPorId(dto.getAgenciaId()));
		contaCorrente.setCliente(clienteService.buscarPorId(dto.getClienteId()));
		return contaCorrente;
	}

	public ContaCorrenteResponseDTO toDto(ContaCorrente contaCorrente) {
		ContaCorrenteResponseDTO responseDTO = new ContaCorrenteResponseDTO();
		BeanUtils.copyProperties(contaCorrente, responseDTO);
		responseDTO.setAgenciaId(contaCorrente.getAgencia().getId());
		responseDTO.setClienteId(contaCorrente.getCliente().getId());
		return responseDTO;
	}

	public List<ContaCorrenteResponseDTO> toListDto(List<ContaCorrente> contas){
		return contas.stream().map(responseDto -> toDto(responseDto)).collect(Collectors.toList());
	}

}
