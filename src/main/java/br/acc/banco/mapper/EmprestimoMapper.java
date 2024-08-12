package br.acc.banco.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import br.acc.banco.dto.emprestimo.EmprestimoCreateDTO;
import br.acc.banco.dto.emprestimo.EmprestimoResponseDTO;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.models.enums.StatusEmprestimo;
import br.acc.banco.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EmprestimoMapper {

	private final ContaCorrenteService contaCorrenteService;
	
	public Emprestimo toEmprestimo(EmprestimoCreateDTO createDTO) {
		Emprestimo emprestimo = new Emprestimo();
		BeanUtils.copyProperties(createDTO, emprestimo);
		emprestimo.setConta(contaCorrenteService.buscarPorId(createDTO.getContaCorrenteId()));
		return emprestimo;
	}
	
	public EmprestimoResponseDTO toDto(Emprestimo emprestimo) {
		EmprestimoResponseDTO responseDTO = new EmprestimoResponseDTO();
		responseDTO.setId(emprestimo.getConta().getId());		
		responseDTO.setQuantidadesParcelasPagas(emprestimo.getQuantidadeParcelasPagas());
		responseDTO.setQuatidadeParcelas(emprestimo.getQuantidadeParcelas());
		responseDTO.setValor(emprestimo.getValor());
		responseDTO.setValorparcela(emprestimo.getValorParcela());
		responseDTO.setStatus(emprestimo.getStatus());
		return responseDTO;
	}
	
	public List<EmprestimoResponseDTO> toListDto(List<Emprestimo> emprestimos) {
		return emprestimos.stream().map(emprestimo -> toDto(emprestimo)).collect(Collectors.toList());
	}
}
