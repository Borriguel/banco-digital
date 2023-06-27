package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.controller.dto.ClienteRequest;
import dev.borriguel.bancodigital.entity.Cliente;
import dev.borriguel.bancodigital.exception.custom.RecursoDuplicadoException;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Cliente criarCliente(ClienteRequest clienteRequest) {
        if (repository.existeClientePorEmail(clienteRequest.getEmail()))
            throw new RecursoDuplicadoException("Já consta em nosso banco de dados este email");
        if (repository.existeClientePorDocumento(clienteRequest.getCpf()))
            throw new RecursoDuplicadoException("Já consta em nosso banco de dados este documento");
        var cliente = modelMapper.map(clienteRequest, Cliente.class);
        repository.save(cliente);
        return cliente;
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente encontrarClientePorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> encontrarTodosClientes(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> encontrarClienteParteNome(String nome, Pageable page) {
        return repository.findByNomeContainsIgnoreCase(nome, page);
    }

    @Override
    @Transactional
    public Cliente atualizarCliente(Long id, ClienteRequest clienteRequest) {
        var cliente = encontrarClientePorId(id);
        if (repository.existeClientePorEmail(clienteRequest.getEmail())) {
            if (!cliente.getEmail().equalsIgnoreCase(clienteRequest.getEmail()))
                throw new RecursoDuplicadoException("Já consta em nosso banco de dados este email");
        }
        if (repository.existeClientePorDocumento(clienteRequest.getCpf())) {
            if (!cliente.getCpf().equals(clienteRequest.getCpf()))
                throw new RecursoDuplicadoException("Já consta em nosso banco de dados este cpf");
        }
        cliente.setNome(clienteRequest.getNome());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setCpf(clienteRequest.getCpf());
        repository.save(cliente);
        return cliente;
    }

    @Override
    @Transactional
    public void deletarCliente(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecursoInvalidoException("Id não encontrado no banco de dados");
        }
    }

    @Override
    public Cliente encontrarClientePorEmail(String email) {
        return repository.encontrarClientePorEmail(email)
                .orElseThrow(() -> new RecursoInvalidoException("Email não encontrado no banco de dados"));
    }
}
