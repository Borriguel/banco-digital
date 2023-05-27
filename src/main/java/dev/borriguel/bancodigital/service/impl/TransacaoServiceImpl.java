package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.controller.dto.ComumDTO;
import dev.borriguel.bancodigital.controller.dto.LojistaDTO;
import dev.borriguel.bancodigital.controller.dto.TransacaoDTO;
import dev.borriguel.bancodigital.controller.dto.TransacaoPost;
import dev.borriguel.bancodigital.entity.Transacao;
import dev.borriguel.bancodigital.exception.custom.ErroTransacaoException;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.ComumRepository;
import dev.borriguel.bancodigital.repository.LojistaRepository;
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
    private final ComumRepository comumRepository;
    private final LojistaRepository lojistaRepository;
    private final ComumService comumService;
    private final LojistaService lojistaService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    @Transactional
    public TransacaoDTO criarTransacao(TransacaoPost transacaoPost) {
        checarTransacao(transacaoPost);
        realizarTransacao(transacaoPost);
        var transacao = modelMapper.map(transacaoPost, Transacao.class);
        transacao.setHora(LocalTime.now());
        transacao.setData(LocalDate.now());
        transacaoRepository.save(transacao);
        logger.info("Transação realizada -> {}", transacao);
        emailService.enviarEmail(transacao);
        return modelMapper.map(transacao, TransacaoDTO.class);
    }

    private void realizarTransacao(TransacaoPost transacaoPost) {
        var pagador = comumRepository.findById(transacaoPost.getIdPagador())
                .orElseThrow(() -> new RecursoInvalidoException("Id pagador não encontrado no banco de dados"));
        var receptorLojista = lojistaRepository.findById(transacaoPost.getIdDeposito());
        var receptorComum = comumRepository.findById(transacaoPost.getIdDeposito());
        if (receptorLojista.isEmpty() && receptorComum.isEmpty())
            throw new RecursoInvalidoException("Id não encontrado para beneficiário.");
        var pagadorDTO = modelMapper.map(pagador, ComumDTO.class);
        comumService.sacar(pagadorDTO, transacaoPost.getValorTransacao());
        if (receptorComum.isPresent()) {
            var comumDTO = modelMapper.map(receptorComum.get(), ComumDTO.class);
            comumService.depositar(comumDTO, transacaoPost.getValorTransacao());
        }
        if (receptorLojista.isPresent()) {
            var lojistaDTO = modelMapper.map(receptorLojista.get(), LojistaDTO.class);
            lojistaService.depositar(lojistaDTO, transacaoPost.getValorTransacao());
        }
    }

    private void checarTransacao(TransacaoPost transacaoDTO) {
        if (transacaoDTO.getValorTransacao().compareTo(BigDecimal.ZERO) <= 0)
            throw new ErroTransacaoException("Insira valor maior que zero para efetuar a transação.");
        if (transacaoDTO.getIdPagador().equals(transacaoDTO.getIdDeposito()))
            throw new RecursoInvalidoException("Não é possível fazer transações pra mesma conta.");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransacaoDTO> encontrarTransacoes(String min, String max, Pageable page) {
        LocalDate dataMin = min.equals("") ? LocalDate.now().minusDays(7) : LocalDate.parse(min);
        LocalDate dataMax = max.equals("") ? LocalDate.now() : LocalDate.parse(max);
        Page<Transacao> transacoes = transacaoRepository.encontrarTransacoes(dataMin, dataMax, page);
        return transacoes.map(x -> modelMapper.map(x, TransacaoDTO.class));
    }
}
