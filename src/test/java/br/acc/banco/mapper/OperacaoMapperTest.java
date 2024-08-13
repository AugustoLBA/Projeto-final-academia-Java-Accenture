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
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Emprestimo;
import br.acc.banco.models.Operacao;
import br.acc.banco.models.Seguro;
import br.acc.banco.models.enums.TipoOperacao;
import br.acc.banco.service.ContaCorrenteService;

@ExtendWith(MockitoExtension.class)
public class OperacaoMapperTest {

    @InjectMocks
    private OperacaoMapper operacaoMapper;

    @Mock
    private ContaCorrenteService contaCorrenteService;

    private ContaCorrente conta;
    private ContaCorrente contaDestino;

    @BeforeEach
    public void setUp() {
        conta = new ContaCorrente();
        conta.setId(1L);
        conta.setNumero(123456);
        conta.setSaldo(BigDecimal.valueOf(1000));
        conta.setChavePix("chaveOrigem");

        contaDestino = new ContaCorrente();
        contaDestino.setId(2L);
        contaDestino.setNumero(654321);
        contaDestino.setSaldo(BigDecimal.valueOf(500));
        contaDestino.setChavePix("chaveDestino");
    }





    @Test
    public void testToOperacaoPix() {
        PixCreateDTO pixCreateDTO = new PixCreateDTO(TipoOperacao.PIX, BigDecimal.valueOf(100), 1L, "chaveOrigem", "chaveDestino");

        when(contaCorrenteService.buscarContaPorChavePix("chaveOrigem")).thenReturn(conta);

        Operacao operacao = operacaoMapper.toOperacao(pixCreateDTO);

        assertEquals(TipoOperacao.PIX, operacao.getTipo());
        assertEquals(conta, operacao.getConta());
        assertEquals("chaveOrigem", operacao.getChavePix());
    }



    @Test
    public void testToOperacaoCompra() {
        CompraCreateDTO compraCreateDTO = new CompraCreateDTO(TipoOperacao.COMPRA, BigDecimal.valueOf(100), 1L, "Loja X");

        when(contaCorrenteService.buscarPorId(1L)).thenReturn(conta);

        Operacao operacao = operacaoMapper.toOperacao(compraCreateDTO);

        assertEquals(TipoOperacao.COMPRA, operacao.getTipo());
        assertEquals(conta, operacao.getConta());
        assertEquals("Loja X", operacao.getNomeEstabelecimento());
    }

    @Test
    public void testToOperacaoGenerica() {
        OperacaoCreateDTO operacaoCreateDTO = new OperacaoCreateDTO(TipoOperacao.SAQUE, BigDecimal.valueOf(100), 1L);

        when(contaCorrenteService.buscarPorId(1L)).thenReturn(conta);

        Operacao operacao = operacaoMapper.toOperacao(operacaoCreateDTO);

        assertEquals(TipoOperacao.SAQUE, operacao.getTipo());
        assertEquals(conta, operacao.getConta());
    }

    @Test
    public void testToDtoTransferencia() {
        Operacao operacao = new Operacao();
        operacao.setId(1L);
        operacao.setTipo(TipoOperacao.TRANSFERENCIA);
        operacao.setConta(conta);
        operacao.setContaDestino(contaDestino);
        operacao.setValor(BigDecimal.valueOf(100));

        TransferenciaResponseDTO dto = (TransferenciaResponseDTO) operacaoMapper.toDto(operacao);

        assertEquals(TipoOperacao.TRANSFERENCIA, dto.getTipo());
        assertEquals(1L, dto.getContaCorrenteId());
        assertEquals(654321, dto.getContaCorrenteDestinoNumero());
    }

    @Test
    public void testToDtoRecebeuTransferencia() {
        Operacao operacao = new Operacao();
        operacao.setId(1L);
        operacao.setTipo(TipoOperacao.RECEBEU_TRANSFERENCIA);
        operacao.setConta(contaDestino);
        operacao.setContaDestino(conta);
        operacao.setValor(BigDecimal.valueOf(100));

        TransferenciaResponseDTO dto = (TransferenciaResponseDTO) operacaoMapper.toDto(operacao);

        assertEquals(TipoOperacao.RECEBEU_TRANSFERENCIA, dto.getTipo());
        assertEquals(2L, dto.getContaCorrenteId());
        assertEquals(123456, dto.getContaCorrenteOrigem());
    }

    @Test
    public void testToDtoPix() {
        Operacao operacao = new Operacao();
        operacao.setId(1L);
        operacao.setTipo(TipoOperacao.PIX);
        operacao.setConta(conta);
        operacao.setContaDestino(contaDestino);
        operacao.setChavePix("chaveOrigem");
        operacao.setValor(BigDecimal.valueOf(100));

        when(contaCorrenteService.buscarContaPorChavePix("chaveDestino")).thenReturn(contaDestino);

        PixResponseDTO dto = (PixResponseDTO) operacaoMapper.toDto(operacao);

        assertEquals(TipoOperacao.PIX, dto.getTipo());
        assertEquals(1L, dto.getContaCorrenteId());
        assertEquals("chaveOrigem", dto.getChavePixOrigem());
        assertEquals("chaveDestino", dto.getChavePixDestino());
    }

    @Test
    public void testToDtoRecebeuPix() {
        Operacao operacao = new Operacao();
        operacao.setId(1L);
        operacao.setTipo(TipoOperacao.RECEBEU_PIX);
        operacao.setConta(contaDestino);
        operacao.setContaDestino(conta);
        operacao.setChavePix("chaveDestino");
        operacao.setValor(BigDecimal.valueOf(100));

        when(contaCorrenteService.buscarContaPorChavePix("chaveOrigem")).thenReturn(conta);

        PixResponseDTO dto = (PixResponseDTO) operacaoMapper.toDto(operacao);

        assertEquals(TipoOperacao.RECEBEU_PIX, dto.getTipo());
        assertEquals(2L, dto.getContaCorrenteId());
        assertEquals("chaveOrigem", dto.getChavePixOrigem());
        assertEquals("chaveDestino", dto.getChavePixDestino());
    }

    @Test
    public void testToDtoCompra() {
        Operacao operacao = new Operacao();
        operacao.setId(1L);
        operacao.setTipo(TipoOperacao.COMPRA);
        operacao.setConta(conta);
        operacao.setNomeEstabelecimento("Loja X");
        operacao.setValor(BigDecimal.valueOf(100));

        CompraResponseDTO dto = (CompraResponseDTO) operacaoMapper.toDto(operacao);

        assertEquals(TipoOperacao.COMPRA, dto.getTipo());
        assertEquals(1L, dto.getContaCorrenteId());
        assertEquals("Loja X", dto.getNomeEstabelecimento());
    }

    @Test
    public void testToDtoParcelaEmprestimo() {
        Operacao operacao = new Operacao();
        operacao.setId(1L);
        operacao.setTipo(TipoOperacao.PARCELA_EMPRESTIMO);
        operacao.setConta(conta);
        operacao.setValor(BigDecimal.valueOf(100));
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(1L);
        operacao.setEmprestimo(emprestimo);

        ParcelaEmprestimoDTO dto = (ParcelaEmprestimoDTO) operacaoMapper.toDto(operacao);

        assertEquals(TipoOperacao.PARCELA_EMPRESTIMO, dto.getTipo());
        assertEquals(1L, dto.getContaCorrenteId());
        assertEquals(1L, dto.getEmprestimoId());
    }

    @Test
    public void testToDtoParcelaSeguro() {
        Operacao operacao = new Operacao();
        operacao.setId(1L);
        operacao.setTipo(TipoOperacao.PARCELA_SEGURO);
        operacao.setConta(conta);
        operacao.setValor(BigDecimal.valueOf(100));
        Seguro seguro = new Seguro();
        seguro.setId(1L);
        operacao.setSeguro(seguro);

        ParcelaSeguroDTO dto = (ParcelaSeguroDTO) operacaoMapper.toDto(operacao);

        assertEquals(TipoOperacao.PARCELA_SEGURO, dto.getTipo());
        assertEquals(1L, dto.getContaCorrenteId());
        assertEquals(1L, dto.getSeguroId());
    }

    @Test
    public void testToDtoGenerica() {
        Operacao operacao = new Operacao();
        operacao.setId(1L);
        operacao.setTipo(TipoOperacao.SAQUE);
        operacao.setConta(conta);
        operacao.setValor(BigDecimal.valueOf(100));

        OperacaoResponseDTO dto = operacaoMapper.toDto(operacao);

        assertEquals(TipoOperacao.SAQUE, dto.getTipo());
        assertEquals(1L, dto.getContaCorrenteId());
    }

    @Test
    public void testToListDto() {
        Operacao operacao1 = new Operacao();
        operacao1.setId(1L);
        operacao1.setTipo(TipoOperacao.SAQUE);
        operacao1.setConta(conta);
        operacao1.setValor(BigDecimal.valueOf(100));

        Operacao operacao2 = new Operacao();
        operacao2.setId(2L);
        operacao2.setTipo(TipoOperacao.DEPOSITO);
        operacao2.setConta(conta);
        operacao2.setValor(BigDecimal.valueOf(200));

        List<Operacao> operacoes = Arrays.asList(operacao1, operacao2);

        List<OperacaoResponseDTO> dtos = operacaoMapper.toListDto(operacoes);

        assertEquals(2, dtos.size());
        assertEquals(TipoOperacao.SAQUE, dtos.get(0).getTipo());
        assertEquals(TipoOperacao.DEPOSITO, dtos.get(1).getTipo());
    }
}
