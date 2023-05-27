package dev.borriguel.bancodigital.service;

import dev.borriguel.bancodigital.controller.dto.TransacaoPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.borriguel.bancodigital.controller.dto.TransacaoDTO;

public interface TransacaoService {
    /**
     * Realiza uma transação entre contas.
     *
     * @param transacaoPost classe modelo para a realização de uma
     *                     transação.
     * @return o objeto {@link TransacaoDTO} contendo as informações da transação.
     */
    TransacaoDTO criarTransacao(TransacaoPost transacaoPost);

    /**
     * Procura transações no banco de dados usando datas.
     * 
     * @param min  data mínima para encontrar transações. Caso nenhum valor seja
     *             informado a data mínima será 7 dias atrás.
     * @param max  data máxima para encontrar transações. Caso nenhum valor seja
     *             informado a data máxima será o dia atual.
     * @param page
     * @return Page do tipo {@link TransacaoDTO}
     */
    Page<TransacaoDTO> encontrarTransacoes(String min, String max, Pageable page);
}
