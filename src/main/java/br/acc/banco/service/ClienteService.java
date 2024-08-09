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
		}catch(DataIntegrityViolationException e) {
			throw new UsernameUniqueViolationException("Cliente com CPF: "+cliente.getCpf()
			+" já cadastrado!");
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

}
