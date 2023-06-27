package dev.borriguel.bancodigital.service;

import java.math.BigDecimal;
import java.util.UUID;

import dev.borriguel.bancodigital.controller.dto.ComumResponse;
import dev.borriguel.bancodigital.entity.Comum;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.exception.custom.SaldoInsuficienteException;

public interface ComumService {
    /**
     * Cria uma conta comum do tipo Pessoa Física e salva no banco de dados.
     *
     * @param idCliente id do cliente que receberá a conta comum.
     * @throws RecursoInvalidoException se o cliente com o ID especificado não for encontrado.
     * @return o objeto {@link ComumResponse} criado.
     */
    Comum criarConta(Long idCliente) throws RecursoInvalidoException;

    /**
     * Atualiza o saldo de uma conta comum do banco de dados.
     *
     * @param conta objeto contendo as informações da conta comum a ser atualizada.
     * @throws RecursoInvalidoException se o cliente com o ID especificado não for encontrado.
     */
    void atualizarSaldo(Comum conta);

    /**
     * Subtrai um valor do saldo de uma conta comum do banco de dados.
     *
     * @param conta objeto contendo as informações da conta comum a ter o valor descontado.
     * @throws SaldoInsuficienteException se o saldo em conta for menor que o valor da transação.
     * @param valor valor que será descontado do saldo da conta comum.
     */
    void sacar(Comum conta, BigDecimal valor) throws SaldoInsuficienteException;

    /**
     * Adiciona um valor ao saldo de uma conta comum do banco de dados.
     *
     * @param conta objeto contendo as informações da conta comum a receber o valor depositado.
     * @param valor valor que será depositado na conta comum.
     */
    void depositar(Comum conta, BigDecimal valor);

    /**
     * Retorna uma conta comum do banco de dados conforme o seu id.
     *
     * @param id o identificador único da conta comum a ser buscada.
     * @throws RecursoInvalidoException se a conta com ID especificado não for encontrado.
     * @return o objeto {@link ComumResponse} correspondente à conta comum buscada.
     */
    Comum encontrarContaComumPorId(UUID id) throws RecursoInvalidoException;
}
