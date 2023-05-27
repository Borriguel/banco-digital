package dev.borriguel.bancodigital.service;

import java.math.BigDecimal;
import java.util.UUID;

import dev.borriguel.bancodigital.controller.dto.ComumDTO;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.exception.custom.SaldoInsuficienteException;

public interface ComumService {
    /**
     * Cria uma conta comum do tipo Pessoa Física e salva no banco de dados.
     *
     * @param idCliente id do cliente que receberá a conta comum.
     * @throws RecursoInvalidoException se o cliente com o ID especificado não for encontrado.
     * @return o objeto {@link ComumDTO} criado.
     */
    ComumDTO criarConta(Long idCliente) throws RecursoInvalidoException;

    /**
     * Atualiza o saldo de uma conta comum do banco de dados.
     *
     * @param conta objeto contendo as informações da conta comum a ser atualizada.
     * @throws RecursoInvalidoException se o cliente com o ID especificado não for encontrado.
     * @return o objeto {@link ComumDTO} com o saldo atualizado.
     */
    ComumDTO atualizarSaldo(ComumDTO conta);

    /**
     * Subtrai um valor do saldo de uma conta comum do banco de dados.
     *
     * @param conta objeto contendo as informações da conta comum a ter o valor descontado.
     * @throws SaldoInsuficienteException se o saldo em conta for menor que o valor da transação.
     * @param valor valor que será descontado do saldo da conta comum.
     */
    void sacar(ComumDTO conta, BigDecimal valor) throws SaldoInsuficienteException;

    /**
     * Adiciona um valor ao saldo de uma conta comum do banco de dados.
     *
     * @param conta objeto contendo as informações da conta comum a receber o valor depositado.
     * @param valor valor que será depositado na conta comum.
     */
    void depositar(ComumDTO conta, BigDecimal valor);

    /**
     * Retorna uma conta comum do banco de dados de acordo com o seu id.
     *
     * @param id o identificador único da conta comum a ser buscada.
     * @throws RecursoInvalidoException se a conta com ID especificado não for encontrado.
     * @return o objeto {@link ComumDTO} correspondente à conta comum buscada.
     */
    ComumDTO encontrarContaComumPorId(UUID id) throws RecursoInvalidoException;
}
