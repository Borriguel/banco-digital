package dev.borriguel.bancodigital.service;

import dev.borriguel.bancodigital.controller.dto.LojistaRequest;
import dev.borriguel.bancodigital.entity.Cliente;
import dev.borriguel.bancodigital.entity.Lojista;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.repository.LojistaRepository;
import dev.borriguel.bancodigital.service.impl.ClienteServiceImpl;
import dev.borriguel.bancodigital.service.impl.LojistaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static dev.borriguel.bancodigital.Util.ClienteFactory.criarCliente;
import static dev.borriguel.bancodigital.Util.LojistaFactory.criarContaLojista;
import static dev.borriguel.bancodigital.Util.LojistaFactory.criarContaLojistaRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para LojistaServiceImpl")
class LojistaServiceTest {
    @Mock
    ClienteServiceImpl clienteService;
    @Mock
    ClienteRepository clienteRepository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    LojistaRepository lojistaRepository;
    @InjectMocks
    LojistaServiceImpl lojistaService;
    private Cliente cliente;
    private LojistaRequest lojistaRequest;
    private Lojista lojista;

    @BeforeEach
    void setUp() {
        cliente = criarCliente();
        lojistaRequest = criarContaLojistaRequest();
        lojista = criarContaLojista();
    }

    @Test
    @DisplayName("Deve atualizar o saldo corretamente")
    void atualizarSaldo() {
        when(lojistaRepository.findById(lojista.getId())).thenReturn(Optional.of(lojista));
        lojistaService.atualizarSaldo(lojista);
        verify(lojistaRepository, times(1)).save(lojista);
    }

    @Test
    @DisplayName("Deve depositar valor na conta e atualizar saldo corretamente")
    void depositar() {
        lojista.setSaldo(BigDecimal.valueOf(500));
        BigDecimal valorDeposito = BigDecimal.valueOf(20);
        when(lojistaRepository.findById(lojista.getId())).thenReturn(Optional.of(lojista));
        lojistaService.depositar(lojista, valorDeposito);
        assertThat(lojista.getSaldo()).isEqualTo(BigDecimal.valueOf(520));
        verify(lojistaRepository, times(1)).save(lojista);
    }

    @Nested
    @DisplayName("Testes para criarConta()")
    class CriarContaTeste {
        @Test
        @DisplayName("Deve criar uma conta com sucesso")
        void criarConta() {
            when(clienteService.encontrarClientePorId(cliente.getId())).thenReturn(cliente);
            when(modelMapper.map(lojistaRequest, Lojista.class)).thenReturn(new Lojista());
            when(clienteRepository.save(cliente)).thenReturn(cliente);
            when(lojistaRepository.save(any(Lojista.class))).thenReturn(lojista);
            var resultado = lojistaService.criarConta(cliente.getId(), lojistaRequest);
            assertThat(resultado).isNotNull();
            verify(lojistaRepository, times(1)).save(any(Lojista.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o cliente não existe")
        void criarContaDeveLancarExcecaoQuandoClienteIdNaoExiste() {
            when(clienteService.encontrarClientePorId(anyLong())).thenThrow(RecursoInvalidoException.class);
            assertThrows(RecursoInvalidoException.class, () -> clienteService.encontrarClientePorId(anyLong()));
        }

        @Test
        @DisplayName("Deve lançar exceção quando já existe uma conta comum associada ao cliente")
        void criarContaDeveLancarExcecaoQuandoExistirContaLojista() {
            cliente.setContaLojista(new Lojista());
            when(clienteService.encontrarClientePorId(cliente.getId())).thenReturn(cliente);
            assertThatThrownBy(() -> lojistaService.criarConta(cliente.getId(), lojistaRequest))
                    .isInstanceOf(RecursoInvalidoException.class)
                    .hasMessage("Só pode ter uma conta lojista.");
        }
    }

    @Nested
    @DisplayName("Testes para encontrarContaLojistaPorId()")
    class EncontrarContaTeste {
        @Test
        @DisplayName("Deve encontrar a conta lojista por ID quando o ID existe no banco de dados")
        void encontrarContaLojistaPorId() {
            when(lojistaRepository.findById(lojista.getId())).thenReturn(Optional.of(lojista));
            var resultado = lojistaService.encontrarContaLojistaPorId(lojista.getId());
            assertThat(resultado).isNotNull();
            verify(lojistaRepository, times(1)).findById(lojista.getId());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID não existe no banco de dados")
        void encontrarContaLojistaPorIdLancaExcecaoQuandoIdNaoExistir() {
            when(lojistaRepository.findById(any())).thenReturn(Optional.empty());
            assertThrows(RecursoInvalidoException.class, () -> lojistaService.encontrarContaLojistaPorId(any()));
            verify(lojistaRepository, times(1)).findById(any());
        }
    }
}