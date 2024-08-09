package br.acc.banco.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.acc.banco.dto.CompraResponseDTO;
import br.acc.banco.dto.OperacaoCreateDTO;
import br.acc.banco.dto.OperacaoResponseDTO;
import br.acc.banco.dto.PixResponseDTO;
import br.acc.banco.dto.TransferenciaResponseDTO;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.enums.TipoOperacao;
import br.acc.banco.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OperacaoMapper {
	
	private final ContaCorrenteService contaCorrenteService;
	
	public Operacao toOperacao(OperacaoCreateDTO dto) {
		Operacao operacao = new Operacao();
		BeanUtils.copyProperties(dto, operacao);
		operacao.setConta(contaCorrenteService.buscarPorId(dto.getContaCorrenteId()));
		return operacao;
	}

	public OperacaoResponseDTO toDto(Operacao operacao) {
		
		if(operacao.getTipo().equals(TipoOperacao.TRANSFERENCIA)) {
			TransferenciaResponseDTO dto = new TransferenciaResponseDTO();
			BeanUtils.copyProperties(operacao, dto);
			dto.setContaCorrenteDestinoId(operacao.getContaDestino().getId());
			dto.setContaCorrenteId(operacao.getConta().getId());
			return dto;
		}
		if(operacao.getTipo().equals(TipoOperacao.PIX)) {
			PixResponseDTO dto = new PixResponseDTO();
			BeanUtils.copyProperties(operacao, dto);
			dto.setContaCorrenteId(operacao.getConta().getId());
			dto.setChavePix(operacao.getChavePix());
			return dto;
		}
		if(operacao.getTipo().equals(TipoOperacao.COMPRA)) {
			CompraResponseDTO dto = new CompraResponseDTO();
			BeanUtils.copyProperties(operacao, dto);
			dto.setContaCorrenteId(operacao.getConta().getId());
			dto.setNomeEstabelecimento(operacao.getNomeEstabelecimento());
			return dto;
		}
		
		OperacaoResponseDTO responseDTO = new OperacaoResponseDTO();
		BeanUtils.copyProperties(operacao, responseDTO);
		responseDTO.setContaCorrenteId(operacao.getConta().getId());
		return responseDTO;
	}

	public List<OperacaoResponseDTO> toListDto(List<Operacao> operacoes){
		return operacoes.stream().map(responseDto -> toDto(responseDto)).collect(Collectors.toList());
	}
	
}
