package br.acc.banco.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.acc.banco.dto.contaCorrente.ContaCorrenteCreateDTO;
import br.acc.banco.dto.contaCorrente.ContaCorrenteResponseDTO;
import br.acc.banco.dto.operacao.CompraCreateDTO;
import br.acc.banco.dto.operacao.OperacaoCreateDTO;
import br.acc.banco.dto.operacao.OperacaoResponseDTO;
import br.acc.banco.dto.operacao.PixCreateDTO;
import br.acc.banco.dto.operacao.TransferenciaCreateDTO;
import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.mapper.ContaCorrenteMapper;
import br.acc.banco.mapper.OperacaoMapper;
import br.acc.banco.models.ContaCorrente;
import br.acc.banco.models.Operacao;
import br.acc.banco.service.ContaCorrenteService;
import br.acc.banco.models.enums.TipoOperacao;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContaCorrenteController.class)
public class ContaCorrenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaCorrenteService contaCorrenteService;

    @MockBean
    private ContaCorrenteMapper contaCorrenteMapper;

    @MockBean
    private OperacaoMapper operacaoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private ContaCorrente contaCorrente;
    private ContaCorrenteResponseDTO contaCorrenteResponseDTO;
    private ContaCorrenteCreateDTO contaCorrenteCreateDTO;
    private Operacao operacao;
    private OperacaoResponseDTO operacaoResponseDTO;

    @BeforeEach
    public void setUp() {
        contaCorrente = new ContaCorrente();
        contaCorrente.setId(1L);
        contaCorrente.setNumero(123456);
        contaCorrente.setSaldo(BigDecimal.valueOf(1000));
        
        Date date = new Date();

        contaCorrenteResponseDTO = new ContaCorrenteResponseDTO(1L, 123456, BigDecimal.valueOf(1000), 1L, 1L, date);
        contaCorrenteCreateDTO = new ContaCorrenteCreateDTO(123456, BigDecimal.valueOf(1000), 1L, 1L);

        operacao = new Operacao();
        operacao.setId(1L);
        operacao.setConta(contaCorrente);
        operacao.setValor(BigDecimal.valueOf(100));
        
        operacaoResponseDTO = new OperacaoResponseDTO(1L, date, TipoOperacao.SAQUE, BigDecimal.valueOf(100), 1L);
    }

    @Test
    public void testSaveContaCorrenteComSucesso() throws Exception {
        when(contaCorrenteMapper.toContaCorrente(any(ContaCorrenteCreateDTO.class))).thenReturn(contaCorrente);
        when(contaCorrenteService.salvar(any(ContaCorrente.class))).thenReturn(contaCorrente);
        when(contaCorrenteMapper.toDto(any(ContaCorrente.class))).thenReturn(contaCorrenteResponseDTO);

        mockMvc.perform(post("/api/banco/contaCorrente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contaCorrenteCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value(123456));
    }

    @Test
    public void testSaveContaCorrenteComDadosInvalidos() throws Exception {
        ContaCorrenteCreateDTO contaInvalida = new ContaCorrenteCreateDTO(-1, BigDecimal.ZERO, 1L, 1L);

        mockMvc.perform(post("/api/banco/contaCorrente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contaInvalida)));
               
    }

    @Test
    public void testFindAllContasCorrentesComSucesso() throws Exception {
        List<ContaCorrente> contaCorrentes = Arrays.asList(contaCorrente);
        List<ContaCorrenteResponseDTO> contasDto = Arrays.asList(contaCorrenteResponseDTO);

        when(contaCorrenteService.buscarTodas()).thenReturn(contaCorrentes);
        when(contaCorrenteMapper.toListDto(contaCorrentes)).thenReturn(contasDto);

        mockMvc.perform(get("/api/banco/contaCorrente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].numero").value(123456));
    }

    @Test
    public void testFindContaCorrenteByIdComSucesso() throws Exception {
        when(contaCorrenteService.buscarPorId(1L)).thenReturn(contaCorrente);
        when(contaCorrenteMapper.toDto(contaCorrente)).thenReturn(contaCorrenteResponseDTO);

        mockMvc.perform(get("/api/banco/contaCorrente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.numero").value(123456));
    }

    @Test
    public void testFindContaCorrenteByIdNaoExistente() throws Exception {
        when(contaCorrenteService.buscarPorId(1L)).thenThrow(new EntityNotFoundException("Conta Corrente não encontrada"));

        mockMvc.perform(get("/api/banco/contaCorrente/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteContaCorrenteByIdComSucesso() throws Exception {
        doNothing().when(contaCorrenteService).deletarPorId(1L);

        mockMvc.perform(delete("/api/banco/contaCorrente/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteContaCorrenteByIdNaoExistente() throws Exception {
        doThrow(new EntityNotFoundException("Conta Corrente não encontrada")).when(contaCorrenteService).deletarPorId(1L);

        mockMvc.perform(delete("/api/banco/contaCorrente/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDepositoComSucesso() throws Exception {
        when(contaCorrenteService.deposito(any(BigDecimal.class), eq(1L))).thenReturn(operacao);
        when(operacaoMapper.toDto(any(Operacao.class))).thenReturn(operacaoResponseDTO);

        OperacaoCreateDTO createDTO = new OperacaoCreateDTO(TipoOperacao.DEPOSITO, BigDecimal.valueOf(100), 1L);

        mockMvc.perform(post("/api/banco/contaCorrente/deposito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(100));
    }

    @Test
    public void testSaqueComSucesso() throws Exception {
        when(contaCorrenteService.sacar(any(BigDecimal.class), eq(1L))).thenReturn(operacao);
        when(operacaoMapper.toDto(any(Operacao.class))).thenReturn(operacaoResponseDTO);

        OperacaoCreateDTO createDTO = new OperacaoCreateDTO(TipoOperacao.SAQUE, BigDecimal.valueOf(100), 1L);

        mockMvc.perform(post("/api/banco/contaCorrente/saque")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(100));
    }

    @Test
    public void testTransferenciaComSucesso() throws Exception {
        when(contaCorrenteService.transferencia(any(BigDecimal.class), eq(1L), eq(2L))).thenReturn(operacao);
        when(operacaoMapper.toDto(any(Operacao.class))).thenReturn(operacaoResponseDTO);

        TransferenciaCreateDTO createDTO = new TransferenciaCreateDTO(TipoOperacao.TRANSFERENCIA, BigDecimal.valueOf(100), 1L, 2L);

        mockMvc.perform(post("/api/banco/contaCorrente/transferencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(100));
    }

    @Test
    public void testCompraComSucesso() throws Exception {
        when(contaCorrenteService.compra(any(BigDecimal.class), eq(1L), eq("Loja X"))).thenReturn(operacao);
        when(operacaoMapper.toDto(any(Operacao.class))).thenReturn(operacaoResponseDTO);

        CompraCreateDTO createDTO = new CompraCreateDTO(TipoOperacao.COMPRA, BigDecimal.valueOf(100), 1L, "Loja X");

        mockMvc.perform(post("/api/banco/contaCorrente/compra")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(100));
    }

    @Test
    public void testPixComSucesso() throws Exception {
        when(contaCorrenteService.pix(any(BigDecimal.class), eq(1L), eq("chave123"))).thenReturn(operacao);
        when(operacaoMapper.toDto(any(Operacao.class))).thenReturn(operacaoResponseDTO);

        PixCreateDTO createDTO = new PixCreateDTO(TipoOperacao.PIX, BigDecimal.valueOf(100), 1L, "chave123");

        mockMvc.perform(post("/api/banco/contaCorrente/pix")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(100));
    }

    @Test
    public void testExibirExtratoComSucesso() throws Exception {
        List<Operacao> operacoes = Arrays.asList(operacao);
        List<OperacaoResponseDTO> operacoesDto = Arrays.asList(operacaoResponseDTO);

        when(contaCorrenteService.exibirExtrato(1L)).thenReturn(operacoes);
        when(operacaoMapper.toListDto(operacoes)).thenReturn(operacoesDto);

        mockMvc.perform(get("/api/banco/contaCorrente/extrato/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].valor").value(100));
    }
}
