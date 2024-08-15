package br.acc.banco.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.acc.banco.models.Agencia;
import br.acc.banco.repository.AgenciaRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final AgenciaRepository agenciaRepository;

    public DataLoader(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe uma agência no banco
        if (agenciaRepository.count() == 0) {
            // Cria e salva uma nova agência
            Agencia agencia = new Agencia(1L, "Agência Central", "83991052300", "Rua Principal, 100", null);
            agenciaRepository.save(agencia);
            System.out.println("Agência criada com sucesso!");
        }
    }
}
