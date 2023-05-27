package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.controller.dto.ClienteDTO;
import dev.borriguel.bancodigital.controller.dto.ClientePost;
import dev.borriguel.bancodigital.entity.Cliente;
import dev.borriguel.bancodigital.exception.custom.RecursoDuplicadoException;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);
    private final ClienteRepository repository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ClienteDTO criarCliente(ClientePost clientePost) {
        if (repository.existeClientePorEmail(clientePost.getEmail()))
            throw new RecursoDuplicadoException("Já consta em nosso banco de dados este email");
        if (repository.existeClientePorDocumento(clientePost.getCpf()))
            throw new RecursoDuplicadoException("Já consta em nosso banco de dados este documento");
        var cliente = modelMapper.map(clientePost, Cliente.class);
        repository.save(cliente);
        logger.info("Cliente criado -> {}", cliente);
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO encontrarClientePorId(Long id) {
        Optional<Cliente> optional = repository.findById(id);
        var cliente = optional
                .orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados"));
        logger.info("Cliente encontrado -> {}", cliente);
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteDTO> encontrarTodosClientes(Pageable page) {
        Page<Cliente> clientes = repository.findAll(page);
        return clientes.map(x -> modelMapper.map(x, ClienteDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteDTO> encontrarClienteParteNome(String nome, Pageable page) {
        Page<Cliente> clientes = repository.findByNomeContainsIgnoreCase(nome, page);
        return clientes.map(x -> modelMapper.map(x, ClienteDTO.class));
    }

    @Override
    @Transactional
    public ClienteDTO atualizarCliente(Long id, ClientePost clientePost) {
        Optional<Cliente> optional = Optional.of(repository.getReferenceById(id));
        Cliente cliente = optional
                .orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados"));
        if (repository.existeClientePorEmail(clientePost.getEmail())) {
            if (!cliente.getEmail().equalsIgnoreCase(clientePost.getEmail()))
                throw new RecursoDuplicadoException("Já consta em nosso banco de dados este email");
        }
        if (repository.existeClientePorDocumento(clientePost.getCpf())) {
            if (!cliente.getCpf().equals(clientePost.getCpf()))
                throw new RecursoDuplicadoException("Já consta em nosso banco de dados este cpf");
        }
        cliente.setNome(clientePost.getNome());
        cliente.setEmail(clientePost.getEmail());
        cliente.setCpf(clientePost.getCpf());
        repository.save(cliente);
        logger.info("Cliente atualizado -> {}", cliente);
        return modelMapper.map(cliente, ClienteDTO.class);
    }

    @Override
    @Transactional
    public void deletarCliente(Long id) {
        try {
            repository.deleteById(id);
            logger.info("Cliente deletado com id -> " + id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecursoInvalidoException("Id não encontrado no banco de dados");
        }
    }

    @Override
    public ClienteDTO encontrarClientePorEmail(String email) {
        Optional<Cliente> optional = repository.encontrarClientePorEmail(email);
        var cliente = optional
                .orElseThrow(() -> new RecursoInvalidoException("Email não encontrado no banco de dados"));
        return modelMapper.map(cliente, ClienteDTO.class);
    }
}
