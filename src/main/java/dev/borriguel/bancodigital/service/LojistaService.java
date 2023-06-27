package dev.borriguel.bancodigital.service;

import java.math.BigDecimal;
import java.util.UUID;

import dev.borriguel.bancodigital.controller.dto.LojistaResponse;
import dev.borriguel.bancodigital.controller.dto.LojistaRequest;
import dev.borriguel.bancodigital.entity.Lojista;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;

public interface LojistaService {
    /**
     * Cria uma conta do tipo Lojista para um determinado cliente e a salva no banco de dados.
     *
     * @param idCliente ID do cliente que receberá a conta lojista.
     * @param lojistaRequest Objeto contendo as informações da conta a ser criada.
     * @throws RecursoInvalidoException se o cliente especificado não existir ou já tiver uma conta lojista associada.
     * @return o objeto {@link LojistaResponse} contendo as informações da conta lojista criada.
     */
    Lojista criarConta(Long idCliente, LojistaRequest lojistaRequest) throws RecursoInvalidoException;

    /**
     * Atualiza o saldo de uma conta lojista.
     *
     * @param lojista Objeto contendo as informações da conta a ser atualizada.
     * @throws RecursoInvalidoException se a conta lojista especificada não existir.
     */
    void atualizarSaldo(Lojista lojista) throws RecursoInvalidoException;

    /**
     * Deposita um determinado valor na conta lojista especificada.
     *
     * @param lojista Objeto contendo as informações da conta lojista que receberá o depósito.
     * @param valor Valor a ser depositado na conta lojista.
     * @throws RecursoInvalidoException se a conta lojista especificada não existir.
     */
    void depositar(Lojista lojista, BigDecimal valor) throws RecursoInvalidoException;

    /**
     * Retorna as informações da conta lojista correspondente ao ID especificado.
     *
     * @param id ID da conta lojista a ser buscada.
     * @throws RecursoInvalidoException se a conta lojista especificada não existir.
     * @return o objeto {@link LojistaResponse} contendo as informações da conta lojista correspondente ao ID especificado.
     */
    Lojista encontrarContaLojistaPorId(UUID id) throws RecursoInvalidoException;
}