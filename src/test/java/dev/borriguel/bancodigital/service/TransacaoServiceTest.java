package dev.borriguel.bancodigital.service;

import dev.borriguel.bancodigital.Util.TransacaoFactory;
import dev.borriguel.bancodigital.controller.dto.TransacaoRequest;
import dev.borriguel.bancodigital.entity.Comum;
import dev.borriguel.bancodigital.entity.Lojista;
import dev.borriguel.bancodigital.entity.Transacao;
import dev.borriguel.bancodigital.exception.custom.ErroTransacaoException;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.TransacaoRepository;
import dev.borriguel.bancodigital.service.impl.TransacaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static dev.borriguel.bancodigital.Util.TransacaoFactory.criarTransacaoRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para TransacaoServiceImpl")
class TransacaoServiceTest {
    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ComumService comumService;

    @Mock
    private LojistaService lojistaService;
    @Mock
    private EmailService emailService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransacaoServiceImpl transacaoService;

    private TransacaoRequest transacaoRequest;
    private Transacao transacao;

    @BeforeEach
    void setUp() {
        transacaoRequest = criarTransacaoRequest();
        transacao = TransacaoFactory.criarTransacao();
    }

    @Test
    @DisplayName("Deve criar uma transação com sucesso")
    void criarTransacao() {
        when(modelMapper.map(any(), any())).thenReturn(transacao);
        when(comumService.encontrarContaComumPorId(any())).thenReturn(new Comum());
        when(lojistaService.encontrarContaLojistaPorId(any())).thenReturn(new Lojista());
        when(transacaoRepository.save(any())).thenReturn(transacao);
        var resultado = transacaoService.criarTransacao(transacaoRequest);
        assertThat(resultado).isNotNull();
        assertThat(resultado.getValorTransacao()).isEqualTo(transacao.getValorTransacao());
        assertThat(resultado.getIdDeposito()).isEqualTo(transacao.getIdDeposito());
        assertThat(resultado.getIdPagador()).isEqualTo(transacao.getIdPagador());
        verify(transacaoRepository, times(1)).save(transacao);
    }

    @Test
    @DisplayName("Deve lançar ErroTransacaoException quando o valor da transação for zero")
    public void criarTransacaoDeveLancarExcecaoQuandoValorTransacaoForZero() {
        transacaoRequest.setValorTransacao(BigDecimal.ZERO);
        assertThrows(ErroTransacaoException.class, () -> transacaoService.criarTransacao(transacaoRequest));
    }

    @Test
    @DisplayName("Deve lançar RecursoInvalidoException quando o id do pagador for igual ao id do deposito")
    void criarTransacaoDeveLancarErroQuandoIdPagadorIgualIdDeposito() {
        var uuid = UUID.randomUUID();
        transacaoRequest.setIdDeposito(uuid);
        transacaoRequest.setIdPagador(uuid);
        assertThrows(RecursoInvalidoException.class, () -> transacaoService.criarTransacao(transacaoRequest));
    }
}