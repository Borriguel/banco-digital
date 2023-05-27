package dev.borriguel.bancodigital.service;

import dev.borriguel.bancodigital.entity.Transacao;

public interface EmailService {
    /**
     * Envia e-mails relacionados a uma transação.
     * <p>
     * Este método é responsável por coordenar o envio de e-mails relacionados a uma transação específica.
     * Ele chama os métodos específicos de envio de e-mail para a conta de depósito e para o pagador da transação.
     *
     * @param transacao a transação para a qual os e-mails serão enviados
     */
    void enviarEmail(Transacao transacao);
}
