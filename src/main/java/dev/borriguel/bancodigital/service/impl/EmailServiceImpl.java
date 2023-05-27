package dev.borriguel.bancodigital.service.impl;

import dev.borriguel.bancodigital.entity.Transacao;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final ClienteRepository clienteRepository;
    private final String email = "banco.digital.borriguel.dev@gmail.com";
    public void enviarEmail(Transacao transacao) {
        enviarEmailContaDeposito(transacao);
        enviarEmailPagador(transacao);
    }
    public void enviarEmailContaDeposito(Transacao transacao) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(email);
        var clienteContaComum = clienteRepository.encontrarClientePeloIdContaComum(transacao.getIdDeposito());
        var clienteContaLojista = clienteRepository.encontrarClientePeloIdContaLojista(transacao.getIdDeposito());
        var clienteDeposito = (clienteContaComum != null) ? clienteContaComum : clienteContaLojista;
        mensagem.setTo(clienteDeposito.getEmail());
        mensagem.setSubject("Você ganhou uma grana!");
        mensagem.setText("Olá " + clienteDeposito.getNome() + "." +
                "\nTransação recebida com sucesso no valor de R$" + transacao.getValorTransacao() + ".");
        mailSender.send(mensagem);
        logger.info("Email enviado para -> {}", clienteDeposito.getEmail());
    }
    public void enviarEmailPagador(Transacao transacao) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(email);
        var clientePagador = clienteRepository.encontrarClientePeloIdContaComum(transacao.getIdPagador());
        mensagem.setTo(clientePagador.getEmail());
        mensagem.setSubject("Transação realizada!");
        mensagem.setText("Olá " + clientePagador.getNome() + "." +
                "\nTransação realizada com sucesso no valor de R$" + transacao.getValorTransacao() + ".");
        mailSender.send(mensagem);
        logger.info("Email enviado para -> {}", clientePagador.getEmail());
    }
}
