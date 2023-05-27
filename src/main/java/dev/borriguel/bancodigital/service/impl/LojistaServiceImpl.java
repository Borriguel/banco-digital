package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.controller.dto.LojistaDTO;
import dev.borriguel.bancodigital.controller.dto.LojistaPost;
import dev.borriguel.bancodigital.entity.Lojista;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.repository.LojistaRepository;
import dev.borriguel.bancodigital.service.LojistaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LojistaServiceImpl implements LojistaService {

    private static final Logger logger = LoggerFactory.getLogger(LojistaServiceImpl.class);
    private final LojistaRepository repository;
    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public LojistaDTO criarConta(Long idCliente, LojistaPost lojistaPost) {
        var optional = clienteRepository.findById(idCliente);
        var cliente = optional.orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados."));
        if (cliente.getContaLojista() != null)
            throw new RecursoInvalidoException("Só pode ter uma conta lojista.");
        var lojista = modelMapper.map(lojistaPost, Lojista.class);
        lojista.setSaldo(BigDecimal.valueOf(5000));
        repository.save(lojista);
        cliente.setContaLojista(lojista);
        clienteRepository.save(cliente);
        logger.info("Conta lojista criada -> {}", lojista);
        return modelMapper.map(lojista, LojistaDTO.class);
    }

    @Override
    @Transactional
    public LojistaDTO atualizarSaldo(LojistaDTO lojistaDTO) {
        var contaAtualizado = repository.getReferenceById(lojistaDTO.getId());
        contaAtualizado.setSaldo(lojistaDTO.getSaldo());
        repository.save(contaAtualizado);
        return modelMapper.map(contaAtualizado, LojistaDTO.class);
    }

    @Override
    public void depositar(LojistaDTO lojistaDTO, BigDecimal valor) {
        BigDecimal somar = lojistaDTO.getSaldo().add(valor);
        lojistaDTO.setSaldo(somar);
        atualizarSaldo(lojistaDTO);
    }

    @Override
    public LojistaDTO encontrarContaLojistaPorId(UUID id) {
        var optional = repository.findById(id);
        var lojista = optional.orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados"));
        logger.info("Conta lojista encontrada -> {}", lojista);
        return modelMapper.map(lojista, LojistaDTO.class);
    }

}
