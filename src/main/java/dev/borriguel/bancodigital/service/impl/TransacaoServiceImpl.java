package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.controller.dto.TransacaoRequest;
import dev.borriguel.bancodigital.controller.dto.TransacaoResponse;
import dev.borriguel.bancodigital.entity.Transacao;
import dev.borriguel.bancodigital.exception.custom.ErroTransacaoException;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.TransacaoRepository;
import dev.borriguel.bancodigital.service.ComumService;
import dev.borriguel.bancodigital.service.EmailService;
import dev.borriguel.bancodigital.service.LojistaService;
import dev.borriguel.bancodigital.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TransacaoServiceImpl implements TransacaoService {
    private static final Logger logger = LoggerFactory.getLogger(TransacaoServiceImpl.class);
    private final TransacaoRepository transacaoRepository;
    private final ComumService comumService;
    private final LojistaService lojistaService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public Transacao criarTransacao(TransacaoRequest transacaoRequest) {
        checarTransacao(transacaoRequest);
        realizarTransacao(transacaoRequest);
        var transacao = modelMapper.map(transacaoRequest, Transacao.class);
        transacao.setHora(LocalTime.now());
        transacao.setData(LocalDate.now());
        transacaoRepository.save(transacao);
        logger.info("Transação realizada -> {}", transacao);
        emailService.enviarEmail(transacao);
        return transacao;
    }

    private void realizarTransacao(TransacaoRequest transacaoRequest) {
        var pagador = comumService.encontrarContaComumPorId(transacaoRequest.getIdPagador());
        try {
            var receptorLojista = lojistaService.encontrarContaLojistaPorId(transacaoRequest.getIdDeposito());
            comumService.sacar(pagador, transacaoRequest.getValorTransacao());
            lojistaService.depositar(receptorLojista, transacaoRequest.getValorTransacao());
        } catch (RecursoInvalidoException e) {
            try {
                var receptorComum = comumService.encontrarContaComumPorId(transacaoRequest.getIdDeposito());
                comumService.sacar(pagador, transacaoRequest.getValorTransacao());
                comumService.depositar(receptorComum, transacaoRequest.getValorTransacao());
            } catch (RecursoInvalidoException ex) {
                throw new RecursoInvalidoException("Id não encontrado para beneficiário");
            }
        }
    }

    private void checarTransacao(TransacaoRequest transacaoRequest) {
        if (transacaoRequest.getValorTransacao().compareTo(BigDecimal.ZERO) <= 0)
            throw new ErroTransacaoException("Insira valor maior que zero para efetuar a transação.");
        if (transacaoRequest.getIdPagador().equals(transacaoRequest.getIdDeposito()))
            throw new RecursoInvalidoException("Não é possível fazer transações pra mesma conta.");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Transacao> encontrarTransacoes(String min, String max, Pageable page) {
        LocalDate dataMin = min.equals("") ? LocalDate.now().minusDays(7) : LocalDate.parse(min);
        LocalDate dataMax = max.equals("") ? LocalDate.now() : LocalDate.parse(max);
        return transacaoRepository.encontrarTransacoes(dataMin, dataMax, page);
    }
}
