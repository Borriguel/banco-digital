package dev.borriguel.bancodigital.service;

import java.math.BigDecimal;
import java.util.UUID;

import dev.borriguel.bancodigital.controller.dto.LojistaDTO;
import dev.borriguel.bancodigital.controller.dto.LojistaPost;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;

public interface LojistaService {
    /**
     * Cria uma conta do tipo Lojista para um determinado cliente e a salva no banco de dados.
     *
     * @param idCliente ID do cliente que receberá a conta lojista.
     * @param lojistaPost Objeto contendo as informações da conta a ser criada.
     * @throws RecursoInvalidoException se o cliente especificado não existir ou já tiver uma conta lojista associada.
     * @return o objeto {@link LojistaDTO} contendo as informações da conta lojista criada.
     */
    LojistaDTO criarConta(Long idCliente, LojistaPost lojistaPost) throws RecursoInvalidoException;

    /**
     * Atualiza o saldo de uma conta lojista.
     *
     * @param lojistaDTO Objeto contendo as informações da conta a ser atualizada.
     * @return o objeto {@link LojistaDTO} contendo as informações da conta lojista com o saldo atualizado.
     * @throws RecursoInvalidoException se a conta lojista especificada não existir.
     */
    LojistaDTO atualizarSaldo(LojistaDTO lojistaDTO) throws RecursoInvalidoException;

    /**
     * Deposita um determinado valor na conta lojista especificada.
     *
     * @param lojistaDTO Objeto contendo as informações da conta lojista que receberá o depósito.
     * @param valor Valor a ser depositado na conta lojista.
     * @throws RecursoInvalidoException se a conta lojista especificada não existir.
     */
    void depositar(LojistaDTO lojistaDTO, BigDecimal valor) throws RecursoInvalidoException;

    /**
     * Retorna as informações da conta lojista correspondente ao ID especificado.
     *
     * @param id ID da conta lojista a ser buscada.
     * @throws RecursoInvalidoException se a conta lojista especificada não existir.
     * @return o objeto {@link LojistaDTO} contendo as informações da conta lojista correspondente ao ID especificado.
     */
    LojistaDTO encontrarContaLojistaPorId(UUID id) throws RecursoInvalidoException;
}