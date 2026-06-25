package br.com.sgc.service;

import br.com.sgc.domain.model.Cliente;
import br.com.sgc.domain.repository.ClienteRepository;
import br.com.sgc.domain.repository.VendaRepository;
import br.com.sgc.dto.ClienteDTO;
import br.com.sgc.exception.BusinessException;
import br.com.sgc.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VendaRepository vendaRepository;

    public ClienteService(ClienteRepository clienteRepository, VendaRepository vendaRepository) {
        this.clienteRepository = clienteRepository;
        this.vendaRepository = vendaRepository;
    }

    public List<Cliente> obterTodos() {
        return clienteRepository.findAll();
    }

    public Cliente obterPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public Cliente salvar(ClienteDTO clienteDTO) {
        // Valida se o CPF já existe
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            throw new BusinessException("CPF já cadastrado na base de dados");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());
        cliente.setEndereco(clienteDTO.getEndereco());

        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = obterPorId(id);

        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());
        cliente.setEndereco(clienteDTO.getEndereco());

        return clienteRepository.save(cliente);
    }

    public void deletar(Long id) {
        obterPorId(id);
        
        // Regra de Negócio: Cliente não pode ser removido se possuir vendas registradas
        if (vendaRepository.existsByClienteId(id)) {
            throw new BusinessException("Cliente não pode ser removido pois possui vendas registradas.");
        }
        
        clienteRepository.deleteById(id);
    }
}
