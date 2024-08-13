package br.acc.banco.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.acc.banco.dto.seguro.SeguroCreateDTO;
import br.acc.banco.dto.seguro.SeguroResponseDTO;
import br.acc.banco.models.Seguro;
import br.acc.banco.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SeguroMapper {

	private final ContaCorrenteService contaCorrenteService;
	
	public Seguro toSeguro(SeguroCreateDTO createDTO) {
		Seguro seguro = new Seguro();
		BeanUtils.copyProperties(createDTO, seguro);
		seguro.setConta(contaCorrenteService.buscarPorId(createDTO.getContaCorrenteId()));
		return seguro;
	}
	
	public SeguroResponseDTO toDto(Seguro seguro) {
		SeguroResponseDTO dto = new SeguroResponseDTO();
		dto.setId(seguro.getConta().getId());		
		dto.setQuantidadesParcelasPagas(seguro.getQuantidadeParcelasPagas());
		dto.setQuatidadeParcelas(seguro.getQuantidadeParcelas());
		dto.setValor(seguro.getValor());
		dto.setValorparcela(seguro.getValorParcela());
		dto.setStatus(seguro.getStatus());
		dto.setTipoSeguro(seguro.getTipo());
		return dto;
	}
	
	public List<SeguroResponseDTO> toListDto(List<Seguro> seguros){
		return seguros.stream().map(seguro -> toDto(seguro)).collect(Collectors.toList());
	}
}
