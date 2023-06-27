package dev.borriguel.bancodigital.service;

import dev.borriguel.bancodigital.controller.dto.TransacaoRequest;
import dev.borriguel.bancodigital.entity.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.borriguel.bancodigital.controller.dto.TransacaoResponse;

public interface TransacaoService {
    /**
     * Realiza uma transação entre contas.
     *
     * @param transacaoRequest classe modelo para a realização de uma
     *                     transação.
     * @return o objeto {@link Transacao} contendo as informações da transação.
     */
    Transacao criarTransacao(TransacaoRequest transacaoRequest);

    /**
     * Procura transações no banco de dados usando datas.
     * 
     * @param min  data mínima para encontrar transações. Caso nenhum valor seja
     *             informado a data mínima será 7 dias atrás.
     * @param max  data máxima para encontrar transações. Caso nenhum valor seja
     *             informado a data máxima será o dia atual.
     * @return Page do tipo {@link TransacaoResponse}
     */
    Page<Transacao> encontrarTransacoes(String min, String max, Pageable page);
}
