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

import br.acc.banco.dto.seguro.SeguroCreateDTO;
import br.acc.banco.dto.seguro.SeguroResponseDTO;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Seguro;
import br.acc.banco.models.enums.StatusSeguro;
import br.acc.banco.models.enums.TipoSeguro;
import br.acc.banco.service.ContaCorrenteService;

@ExtendWith(MockitoExtension.class)
public class SeguroMapperTest {

    @InjectMocks
    private SeguroMapper seguroMapper;

    @Mock
    private ContaCorrenteService contaCorrenteService;

    private ContaCorrente contaCorrente;
    private Seguro seguro;

    @BeforeEach
    public void setUp() {
        contaCorrente = new ContaCorrente();
        contaCorrente.setId(1L);
        contaCorrente.setNumero(123456);
        contaCorrente.setSaldo(BigDecimal.valueOf(1000));

        seguro = new Seguro();
        seguro.setId(1L);
        seguro.setConta(contaCorrente);
        seguro.setQuantidadeParcelas(10);
        seguro.setQuantidadeParcelasPagas(3);
        seguro.setValor(BigDecimal.valueOf(500));
        seguro.setValorParcela(BigDecimal.valueOf(50));
        seguro.setStatus(StatusSeguro.ATIVO);
        seguro.setTipo(TipoSeguro.VIDA);
    }

    @Test
    public void testToSeguro() {
        SeguroCreateDTO createDTO = new SeguroCreateDTO(BigDecimal.valueOf(500), 10, TipoSeguro.VIDA, 1L);

        when(contaCorrenteService.buscarPorId(1L)).thenReturn(contaCorrente);

        Seguro seguroMapped = seguroMapper.toSeguro(createDTO);

        assertEquals(BigDecimal.valueOf(500), seguroMapped.getValor());
        assertEquals(10, seguroMapped.getQuantidadeParcelas());
        assertEquals(TipoSeguro.VIDA, seguroMapped.getTipo());
        assertEquals(contaCorrente, seguroMapped.getConta());
    }

    @Test
    public void testToDto() {
        SeguroResponseDTO dto = seguroMapper.toDto(seguro);

        assertEquals(1L, dto.getId());
        assertEquals(BigDecimal.valueOf(500), dto.getValor());
        assertEquals(10, dto.getQuatidadeParcelas());
        assertEquals(3, dto.getQuantidadesParcelasPagas());
        assertEquals(BigDecimal.valueOf(50), dto.getValorparcela());
        assertEquals(StatusSeguro.ATIVO, dto.getStatus());
        assertEquals(TipoSeguro.VIDA, dto.getTipoSeguro());
    }

    @Test
    public void testToListDto() {
        Seguro seguro2 = new Seguro();
        seguro2.setId(2L);
        seguro2.setQuantidadeParcelas(12);
        seguro2.setQuantidadeParcelasPagas(5);
        seguro2.setValor(BigDecimal.valueOf(600));
        seguro2.setValorParcela(BigDecimal.valueOf(50));
        seguro2.setStatus(StatusSeguro.CANCELADO);
        seguro2.setTipo(TipoSeguro.BENS);

        List<Seguro> seguros = Arrays.asList(seguro, seguro2);

        List<SeguroResponseDTO> dtos = seguroMapper.toListDto(seguros);

        assertEquals(2, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
        assertEquals(2L, dtos.get(1).getId());
        assertEquals(StatusSeguro.CANCELADO, dtos.get(1).getStatus());
        assertEquals(TipoSeguro.BENS, dtos.get(1).getTipoSeguro());
    }
}
