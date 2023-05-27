package dev.borriguel.bancodigital.exception.custom;

public class ErroTransacaoException extends RuntimeException {
    /**
     * ErroTransacaoException é lançado quando uma transação não está nos conformes.
     * Ex.: Transação com valor negativo ou nulo.
     * 
     * @param mensagem
     */
    public ErroTransacaoException(String mensagem) {
        super(mensagem);
    }
}
