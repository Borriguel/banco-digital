package dev.borriguel.bancodigital.service;

import dev.borriguel.bancodigital.entity.Cliente;
import dev.borriguel.bancodigital.entity.Comum;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.exception.custom.SaldoInsuficienteException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.repository.ComumRepository;
import dev.borriguel.bancodigital.service.impl.ClienteServiceImpl;
import dev.borriguel.bancodigital.service.impl.ComumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static dev.borriguel.bancodigital.Util.ClienteFactory.criarCliente;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ComumServiceImpl")
class ComumServiceTest {
    @Mock
    ClienteServiceImpl clienteService;
    @Mock
    ClienteRepository clienteRepository;
    @Mock
    ComumRepository comumRepository;
    @InjectMocks
    ComumServiceImpl comumService;
    private Cliente cliente;
    private Comum contaComum;

    @BeforeEach
    void setUp() {
        cliente = criarCliente();
        contaComum = new Comum();
    }

    @Test
    @DisplayName("Deve atualizar o saldo corretamente")
    void testAtualizarSaldo() {
        contaComum.setSaldo(BigDecimal.valueOf(100));
        contaComum.setId(UUID.randomUUID());
        Comum contaAtualizada = new Comum();
        contaAtualizada.setSaldo(BigDecimal.valueOf(200));
        contaAtualizada.setId(contaComum.getId());
        when(comumRepository.findById(contaComum.getId())).thenReturn(Optional.of(contaComum));
        comumService.atualizarSaldo(contaAtualizada);
        assertThat(contaComum.getSaldo()).isEqualTo(BigDecimal.valueOf(200));
        verify(comumRepository, times(1)).save(contaAtualizada);
    }

    @Test
    @DisplayName("Deve depositar valor na conta e atualizar saldo corretamente")
    void depositar() {
        contaComum.setId(UUID.randomUUID());
        BigDecimal valor = BigDecimal.valueOf(500);
        BigDecimal saldoAntesDoDeposito = contaComum.getSaldo();
        when(comumRepository.findById(contaComum.getId())).thenReturn(Optional.of(contaComum));
        comumService.depositar(contaComum, valor);
        BigDecimal valorEsperado = saldoAntesDoDeposito.add(valor);
        assertThat(valorEsperado).isEqualTo(contaComum.getSaldo());
    }

    @Nested
    @DisplayName("Testes para criarConta()")
    class CriarContaTeste {
        @Test
        @DisplayName("Deve criar uma conta com sucesso")
        void criarContaDeveCriarContaComSucesso() {
            when(clienteService.encontrarClientePorId(cliente.getId())).thenReturn(cliente);
            when(comumRepository.save(any(Comum.class))).thenReturn(contaComum);
            var contaCriada = comumService.criarConta(cliente.getId());
            assertThat(contaCriada).isNotNull();
            assertThat(contaCriada.getSaldo()).isEqualTo(contaComum.getSaldo());
            verify(comumRepository, times(1)).save(any(Comum.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando o cliente não existe")
        void criarContaDeveLancarExcecaoQuandoClienteIdNaoExiste() {
            when(clienteService.encontrarClientePorId(anyLong())).thenThrow(RecursoInvalidoException.class);
            assertThrows(RecursoInvalidoException.class, () -> clienteService.encontrarClientePorId(anyLong()));
        }

        @Test
        @DisplayName("Deve lançar exceção quando já existe uma conta comum associada ao cliente")
        void criarContaDeveLancarExcecaoQuandoExistirContaComum() {
            cliente.setContaComum(new Comum());
            when(clienteService.encontrarClientePorId(cliente.getId())).thenReturn(cliente);
            assertThatThrownBy(() -> comumService.criarConta(cliente.getId()))
                    .isInstanceOf(RecursoInvalidoException.class)
                    .hasMessage("Só pode ter uma conta comum.");
        }
    }

    @Nested
    @DisplayName("Testes para sacar()")
    class SacarValorTeste {
        @Test
        @DisplayName("Deve sacar valor da conta e atualizar saldo corretamente")
        void deveSacarValorDaConta() {
            contaComum.setId(UUID.randomUUID());
            BigDecimal valor = BigDecimal.valueOf(500);
            BigDecimal saldoAntesDoSaque = contaComum.getSaldo();
            when(comumRepository.findById(contaComum.getId())).thenReturn(Optional.of(contaComum));
            comumService.sacar(contaComum, valor);
            BigDecimal valorEsperado = saldoAntesDoSaque.subtract(valor);
            assertThat(valorEsperado).isEqualTo(contaComum.getSaldo());
        }

        @Test
        @DisplayName("Deve lançar exceção quando saldo for insuficiente")
        void deveLancarExcecaoQuandoSaldoInsuficiente() {
            contaComum.setSaldo(BigDecimal.valueOf(200));
            BigDecimal valor = BigDecimal.valueOf(500);
            assertThrows(SaldoInsuficienteException.class, () -> comumService.sacar(contaComum, valor));
        }
    }

    @Nested
    @DisplayName("Testes para encontrarContaComumPorId()")
    class EncontrarContaTeste {
        @Test
        @DisplayName("Deve encontrar a conta comum por ID quando o ID existe no banco de dados")
        void encontrarContaComumPorIdQuandoIdExistir() {
            contaComum.setId(UUID.randomUUID());
            when(comumRepository.findById(contaComum.getId())).thenReturn(Optional.of(contaComum));
            var resultado = comumService.encontrarContaComumPorId(contaComum.getId());
            assertNotNull(resultado);
            assertThat(resultado).isEqualTo(contaComum);
            verify(comumRepository, times(1)).findById(contaComum.getId());
        }

        @Test
        @DisplayName("Deve lançar exceção quando o ID não existe no banco de dados")
        void encontrarContaComumPorIdLancaExcecaoQuandoIdNaoExiste() {
            when(comumRepository.findById(any())).thenReturn(Optional.empty());
            assertThrows(RecursoInvalidoException.class, () -> comumService.encontrarContaComumPorId(any()));
            verify(comumRepository, times(1)).findById(any());
        }
    }
}