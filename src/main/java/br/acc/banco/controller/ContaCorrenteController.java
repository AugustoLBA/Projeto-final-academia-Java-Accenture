package br.acc.banco.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.acc.banco.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@RestController
@RequestMapping("api/banco/contaCorrente")
public class ContaCorrenteController {

	private final ContaCorrenteService contaCorrenteService;
}
