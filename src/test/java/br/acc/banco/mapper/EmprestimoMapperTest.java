package br.acc.banco.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.acc.banco.dto.emprestimo.EmprestimoCreateDTO;
import br.acc.banco.dto.emprestimo.EmprestimoResponseDTO;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.models.enums.StatusEmprestimo;
import br.acc.banco.service.ContaCorrenteService;

@ExtendWith(MockitoExtension.class)
public class EmprestimoMapperTest {

    @InjectMocks
    private EmprestimoMapper emprestimoMapper;

    @Mock
    private ContaCorrenteService contaCorrenteService;

    private ContaCorrente contaCorrente;
    private Emprestimo emprestimo;

    @BeforeEach
    public void setUp() {
        contaCorrente = new ContaCorrente();
        contaCorrente.setId(1L);
        contaCorrente.setNumero(123456);
        contaCorrente.setSaldo(BigDecimal.valueOf(1000));

        emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        emprestimo.setConta(contaCorrente);
        emprestimo.setQuantidadeParcelas(10);
        emprestimo.setQuantidadeParcelasPagas(3);
        emprestimo.setValor(BigDecimal.valueOf(5000));
        emprestimo.setValorParcela(BigDecimal.valueOf(500));
        emprestimo.setStatus(StatusEmprestimo.APROVADO);
    }

    @Test
    public void testToEmprestimo() {
        EmprestimoCreateDTO createDTO = new EmprestimoCreateDTO(BigDecimal.valueOf(5000), 10, 1L);

        when(contaCorrenteService.buscarPorId(1L)).thenReturn(contaCorrente);

        Emprestimo emprestimoMapped = emprestimoMapper.toEmprestimo(createDTO);

        assertEquals(BigDecimal.valueOf(5000), emprestimoMapped.getValor());
        assertEquals(10, emprestimoMapped.getQuantidadeParcelas());
        assertEquals(contaCorrente, emprestimoMapped.getConta());
    }

    @Test
    public void testToDto() {
        EmprestimoResponseDTO dto = emprestimoMapper.toDto(emprestimo);

        assertEquals(1L, dto.getId());
        assertEquals(BigDecimal.valueOf(5000), dto.getValor());
        assertEquals(10, dto.getQuatidadeParcelas());
        assertEquals(3, dto.getQuantidadesParcelasPagas());
        assertEquals(BigDecimal.valueOf(500), dto.getValorparcela());
        assertEquals(StatusEmprestimo.APROVADO, dto.getStatus());
    }

    @Test
    public void testToListDto() {
        Emprestimo emprestimo2 = new Emprestimo();
        emprestimo2.setId(2L);
        emprestimo2.setQuantidadeParcelas(12);
        emprestimo2.setQuantidadeParcelasPagas(5);
        emprestimo2.setValor(BigDecimal.valueOf(6000));
        emprestimo2.setValorParcela(BigDecimal.valueOf(500));
        emprestimo2.setStatus(StatusEmprestimo.APROVADO);

        List<Emprestimo> emprestimos = Arrays.asList(emprestimo, emprestimo2);

        List<EmprestimoResponseDTO> dtos = emprestimoMapper.toListDto(emprestimos);

        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals(StatusEmprestimo.APROVADO, dtos.get(1).getStatus());
        assertEquals(BigDecimal.valueOf(6000), dtos.get(1).getValor());
    }
}
