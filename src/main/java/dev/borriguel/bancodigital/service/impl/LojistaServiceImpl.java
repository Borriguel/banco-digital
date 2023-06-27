package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.controller.dto.LojistaRequest;
import dev.borriguel.bancodigital.entity.Lojista;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.repository.LojistaRepository;
import dev.borriguel.bancodigital.service.ClienteService;
import dev.borriguel.bancodigital.service.LojistaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LojistaServiceImpl implements LojistaService {

    private final LojistaRepository repository;
    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Lojista criarConta(Long idCliente, LojistaRequest lojistaRequest) {
        var cliente = clienteService.encontrarClientePorId(idCliente);
        if (cliente.getContaLojista() != null)
            throw new RecursoInvalidoException("Só pode ter uma conta lojista.");
        var lojista = modelMapper.map(lojistaRequest, Lojista.class);
        repository.save(lojista);
        cliente.setContaLojista(lojista);
        clienteRepository.save(cliente);
        depositar(lojista, BigDecimal.valueOf(5000));
        return lojista;
    }

    @Override
    @Transactional
    public void atualizarSaldo(Lojista lojista) {
        var contaAtualizada = encontrarContaLojistaPorId(lojista.getId());
        contaAtualizada.setSaldo(lojista.getSaldo());
        repository.save(contaAtualizada);
    }

    @Override
    public void depositar(Lojista lojista, BigDecimal valor) {
        BigDecimal somar = lojista.getSaldo().add(valor);
        lojista.setSaldo(somar);
        atualizarSaldo(lojista);
    }

    @Override
    public Lojista encontrarContaLojistaPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados"));
    }

}
