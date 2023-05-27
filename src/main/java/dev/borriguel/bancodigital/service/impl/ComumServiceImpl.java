package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.controller.dto.ComumDTO;
import dev.borriguel.bancodigital.entity.Comum;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.exception.custom.SaldoInsuficienteException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.repository.ComumRepository;
import dev.borriguel.bancodigital.service.ComumService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComumServiceImpl implements ComumService {
    private static final Logger logger = LoggerFactory.getLogger(ComumServiceImpl.class);
    private final ClienteRepository clienteRepository;
    private final ComumRepository repository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ComumDTO criarConta(Long idCliente) {
        var optional = clienteRepository.findById(idCliente);
        var cliente = optional.orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados."));
        if (cliente.getContaComum() != null)
            throw new RecursoInvalidoException("Só pode ter uma conta comum.");
        var comum = new Comum();
        comum.setSaldo(BigDecimal.valueOf(10000));
        repository.save(comum);
        cliente.setContaComum(comum);
        clienteRepository.save(cliente);
        logger.info("Conta comum criada -> " + comum);
        return modelMapper.map(comum, ComumDTO.class);
    }

    @Override
    @Transactional
    public ComumDTO atualizarSaldo(ComumDTO comumDTO) {
        var contaAtualizada = repository.getReferenceById(comumDTO.getId());
        contaAtualizada.setSaldo(comumDTO.getSaldo());
        repository.save(contaAtualizada);
        return modelMapper.map(contaAtualizada, ComumDTO.class);
    }

    @Override
    public void depositar(ComumDTO comumDTO, BigDecimal valor) {
        BigDecimal somar = comumDTO.getSaldo().add(valor);
        comumDTO.setSaldo(somar);
        atualizarSaldo(comumDTO);
    }

    @Override
    public void sacar(ComumDTO conta, BigDecimal valor) {
        if (conta.getSaldo().compareTo(valor) < 0)
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar transação. Diferença de R$"
                    + conta.getSaldo().subtract(valor).abs());
        BigDecimal subtrair = conta.getSaldo().subtract(valor);
        conta.setSaldo(subtrair);
        atualizarSaldo(conta);
    }

    @Override
    @Transactional(readOnly = true)
    public ComumDTO encontrarContaComumPorId(UUID id) {
        Optional<Comum> optional = repository.findById(id);
        var comum = optional.orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados"));
        logger.info("Conta comum encontrada -> " + comum);
        return modelMapper.map(comum, ComumDTO.class);
    }

}
