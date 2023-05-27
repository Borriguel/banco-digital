package dev.borriguel.bancodigital.exception.custom;

public class SaldoInsuficienteException extends RuntimeException {
    /**
     * SaldoInsuficienteException é lançado quando uma conta tenta realizar uma
     * transação com valor maior que o saldo disponível.
     * 
     * @param mensagem
     */
    public SaldoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
