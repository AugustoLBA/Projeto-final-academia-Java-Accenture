package br.acc.banco.service;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.acc.banco.exception.EntityNotFoundException;
import br.acc.banco.exception.UsernameUniqueViolationException;
import br.acc.banco.models.Cliente;
import br.acc.banco.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Injeção de depêndencia via lombok
@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;

	public Cliente salvar(Cliente cliente) {
	    try {
	        return clienteRepository.save(cliente);
	    } catch (DataIntegrityViolationException e) {
	        String message;
	        
	        if (e.getMessage().contains("CPF")) {
	            message = "Cliente com CPF: " + cliente.getCpf() + " já cadastrado ou CPF inválido!";
	        } else if (e.getMessage().contains("telefone")) {
	            message = "Cliente com telefone: " + cliente.getTelefone() + " já cadastrado ou telefone inválido!";
	        } else {
	            message = "Erro ao salvar cliente: " + e.getMessage();
	        }
	        
	        throw new UsernameUniqueViolationException(message);
	    }
	}

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Cliente com " + id + " não encontrado!"));
	}

	public List<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	public void deletarPorId(Long id) {
		Cliente cliente = buscarPorId(id);
		clienteRepository.delete(cliente);
	}
	
	public Cliente buscarPorCPF(String cpf) {
		return clienteRepository.findByCpf(cpf).
				orElseThrow(() -> new EntityNotFoundException("Cliente com CPF: "+cpf+" não encontrado !"));
	}

}
