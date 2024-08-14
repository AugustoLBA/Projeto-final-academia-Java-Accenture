package br.acc.banco.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.acc.banco.dto.operacao.CompraCreateDTO;
import br.acc.banco.dto.operacao.CompraResponseDTO;
import br.acc.banco.dto.operacao.OperacaoCreateDTO;
import br.acc.banco.dto.operacao.OperacaoResponseDTO;
import br.acc.banco.dto.operacao.ParcelaEmprestimoDTO;
import br.acc.banco.dto.operacao.ParcelaSeguroDTO;
import br.acc.banco.dto.operacao.PixCreateDTO;
import br.acc.banco.dto.operacao.PixResponseDTO;
import br.acc.banco.dto.operacao.TransferenciaCreateDTO;
import br.acc.banco.dto.operacao.TransferenciaResponseDTO;
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
		
		if(dto.getTipo().equals(TipoOperacao.TRANSFERENCIA)) {
			TransferenciaCreateDTO transferencia = (TransferenciaCreateDTO) dto;
			BeanUtils.copyProperties(transferencia, operacao);
			operacao.setConta(contaCorrenteService.buscarContaPorNumero(
					operacao.getConta().getNumero()));
			operacao.setContaDestino(contaCorrenteService.buscarContaPorNumero(
					operacao.getContaDestino().getNumero()));
			return operacao;
		}
		if(dto.getTipo().equals(TipoOperacao.RECEBEU_TRANSFERENCIA)) {
			TransferenciaCreateDTO transferencia = (TransferenciaCreateDTO) dto;
			BeanUtils.copyProperties(transferencia, operacao);
			operacao.setConta(contaCorrenteService.buscarContaPorNumero(
					operacao.getContaDestino().getNumero()));
			operacao.setContaDestino(contaCorrenteService.buscarContaPorNumero(
					operacao.getConta().getNumero()));
			return operacao;
		}
		if(dto.getTipo().equals(TipoOperacao.PIX)) {
			PixCreateDTO pix = (PixCreateDTO) dto;
			BeanUtils.copyProperties(pix, operacao);
			operacao.setConta(contaCorrenteService.buscarContaPorChavePix(pix.getChavePixOrigem()));
			operacao.setChavePix(pix.getChavePixOrigem());
			return operacao;
		}
		if(dto.getTipo().equals(TipoOperacao.RECEBEU_PIX)) {
			PixCreateDTO pix = (PixCreateDTO) dto;
			BeanUtils.copyProperties(pix, operacao);
			operacao.setConta(contaCorrenteService.buscarContaPorChavePix(pix.getChavePixDestino()));
			operacao.setChavePix(pix.getChavePixDestino());
			return operacao;
		}
		if(dto.getTipo().equals(TipoOperacao.COMPRA)) {
			CompraCreateDTO compra = (CompraCreateDTO) dto;
			BeanUtils.copyProperties(compra, operacao);
			operacao.setConta(contaCorrenteService.buscarPorId(compra.getContaCorrenteId()));
			operacao.setNomeEstabelecimento(compra.getNomeEstabelecimento());
			return operacao;
		}
		
		BeanUtils.copyProperties(dto, operacao);
		operacao.setConta(contaCorrenteService.buscarPorId(dto.getContaCorrenteId()));
		return operacao;
	}

	public OperacaoResponseDTO toDto(Operacao operacao) {
		
		if(operacao.getTipo().equals(TipoOperacao.TRANSFERENCIA)) {
			TransferenciaResponseDTO dto = new TransferenciaResponseDTO();
			BeanUtils.copyProperties(operacao, dto);
			dto.setContaCorrenteId(operacao.getConta().getId());
			dto.setContaCorrenteDestinoNumero(operacao.getContaDestino().getNumero());
			dto.setContaCorrenteOrigem(operacao.getConta().getNumero());
			return dto;
		}
		if(operacao.getTipo().equals(TipoOperacao.RECEBEU_TRANSFERENCIA)) {
			TransferenciaResponseDTO dto = new TransferenciaResponseDTO();
			BeanUtils.copyProperties(operacao, dto);
			dto.setContaCorrenteId(operacao.getConta().getId());
			dto.setContaCorrenteDestinoNumero(operacao.getConta().getNumero());
			dto.setContaCorrenteOrigem(operacao.getContaDestino().getNumero());
			return dto;
		}
		if(operacao.getTipo().equals(TipoOperacao.PIX)) {
			PixResponseDTO dto = new PixResponseDTO();
			BeanUtils.copyProperties(operacao, dto);
			dto.setContaCorrenteId(operacao.getConta().getId());
			dto.setChavePixOrigem(operacao.getChavePix());
			dto.setChavePixDestino(contaCorrenteService.buscarContaPorChavePix(
					operacao.getContaDestino().getChavePix()).getChavePix());
			return dto;
		}
		if(operacao.getTipo().equals(TipoOperacao.RECEBEU_PIX)) {
			PixResponseDTO dto = new PixResponseDTO();
			BeanUtils.copyProperties(operacao, dto);
			dto.setContaCorrenteId(operacao.getConta().getId());
			dto.setChavePixOrigem(contaCorrenteService.buscarContaPorChavePix(
					operacao.getContaDestino().getChavePix()).getChavePix());
			
			dto.setChavePixDestino(operacao.getChavePix());
			return dto;
		}
		if(operacao.getTipo().equals(TipoOperacao.COMPRA)) {
			CompraResponseDTO dto = new CompraResponseDTO();
			BeanUtils.copyProperties(operacao, dto);
			dto.setContaCorrenteId(operacao.getConta().getId());
			dto.setNomeEstabelecimento(operacao.getNomeEstabelecimento());
			return dto;
		}
		if(operacao.getTipo().equals(TipoOperacao.PARCELA_EMPRESTIMO)) {
			ParcelaEmprestimoDTO parcelaEmprestimo = new ParcelaEmprestimoDTO();
			parcelaEmprestimo.setId(operacao.getId());
			parcelaEmprestimo.setDataRealizada(operacao.getDataRealizada());
			parcelaEmprestimo.setTipo(operacao.getTipo());
			parcelaEmprestimo.setValor(operacao.getValor());
			parcelaEmprestimo.setContaCorrenteId(operacao.getConta().getId());
			parcelaEmprestimo.setEmprestimoId(operacao.getEmprestimo().getId());
			return parcelaEmprestimo;
		}
		if(operacao.getTipo().equals(TipoOperacao.PARCELA_SEGURO)) {
			ParcelaSeguroDTO parcelaSeguroDTO = new ParcelaSeguroDTO();
			parcelaSeguroDTO.setId(operacao.getId());
			parcelaSeguroDTO.setDataRealizada(operacao.getDataRealizada());
			parcelaSeguroDTO.setTipo(operacao.getTipo());
			parcelaSeguroDTO.setValor(operacao.getValor());
			parcelaSeguroDTO.setContaCorrenteId(operacao.getConta().getId());
			parcelaSeguroDTO.setSeguroId(operacao.getSeguro().getId());
			return parcelaSeguroDTO;
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
