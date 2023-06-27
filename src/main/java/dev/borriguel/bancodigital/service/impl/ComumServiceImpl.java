package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.entity.Comum;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.exception.custom.SaldoInsuficienteException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.repository.ComumRepository;
import dev.borriguel.bancodigital.service.ClienteService;
import dev.borriguel.bancodigital.service.ComumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComumServiceImpl implements ComumService {
    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;
    private final ComumRepository repository;

    @Override
    @Transactional
    public Comum criarConta(Long idCliente) {
        var cliente = clienteService.encontrarClientePorId(idCliente);
        if (cliente.getContaComum() != null)
            throw new RecursoInvalidoException("Só pode ter uma conta comum.");
        var comum = new Comum();
        repository.save(comum);
        cliente.setContaComum(comum);
        clienteRepository.save(cliente);
        depositar(comum, BigDecimal.valueOf(10000));
        return comum;
    }

    @Override
    @Transactional
    public void atualizarSaldo(Comum comum) {
        var contaAtualizada = encontrarContaComumPorId(comum.getId());
        contaAtualizada.setSaldo(comum.getSaldo());
        repository.save(contaAtualizada);
    }

    @Override
    public void depositar(Comum comum, BigDecimal valor) {
        BigDecimal somar = comum.getSaldo().add(valor);
        comum.setSaldo(somar);
        atualizarSaldo(comum);
    }

    @Override
    public void sacar(Comum conta, BigDecimal valor) {
        if (conta.getSaldo().compareTo(valor) < 0)
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar transação. Diferença de R$"
                    + conta.getSaldo().subtract(valor).abs());
        BigDecimal subtrair = conta.getSaldo().subtract(valor);
        conta.setSaldo(subtrair);
        atualizarSaldo(conta);
    }

    @Override
    @Transactional(readOnly = true)
    public Comum encontrarContaComumPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RecursoInvalidoException("Id não encontrado no banco de dados"));
    }

}
