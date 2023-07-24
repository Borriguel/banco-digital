package dev.borriguel.bancodigital.service;

import dev.borriguel.bancodigital.controller.dto.ClienteRequest;
import dev.borriguel.bancodigital.entity.Cliente;
import dev.borriguel.bancodigital.exception.custom.RecursoDuplicadoException;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import dev.borriguel.bancodigital.repository.ClienteRepository;
import dev.borriguel.bancodigital.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static dev.borriguel.bancodigital.Util.ClienteFactory.criarCliente;
import static dev.borriguel.bancodigital.Util.ClienteFactory.criarClienteRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ClienteServiceImpl")
class ClienteServiceTest {
    @InjectMocks
    ClienteServiceImpl clienteService;
    @Mock
    ClienteRepository clienteRepository;
    @Mock
    ModelMapper modelMapper;

    private ClienteRequest clienteRequest;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteRequest = criarClienteRequest();
        cliente = criarCliente();
    }

    @Test
    @DisplayName("Deletar cliente")
    void deletarCliente() {
        clienteService.deletarCliente(cliente.getId());
        verify(clienteRepository, times(1)).deleteById(cliente.getId());
    }

    @Nested
    @DisplayName("Testes para criarCliente()")
    class CriarClienteTeste {
        @Test
        @DisplayName("Criar cliente deve criar cliente quando CPF e email não existem no banco")
        void criarClienteDeveCriarClienteQuandoCPFeEmailNaoExistemNoBanco() {
            when(clienteRepository.existeClientePorEmail(clienteRequest.getEmail())).thenReturn(false);
            when(clienteRepository.existeClientePorDocumento(clienteRequest.getCpf())).thenReturn(false);
            when(modelMapper.map(clienteRequest, Cliente.class)).thenReturn(cliente);
            var resultado = clienteService.criarCliente(clienteRequest);

            assertNotNull(resultado);
            assertThat(resultado.getNome()).isEqualTo(cliente.getNome());
            assertThat(resultado.getCpf()).isEqualTo(cliente.getCpf());
            assertThat(resultado.getEmail()).isEqualTo(cliente.getEmail());
            verify(clienteRepository, times(1)).save(cliente);
        }

        @Test
        @DisplayName("Criar cliente quando email já existe deve lançar exceção")
        void criarClienteQuandoEmailJaExisteDeveLancarExcecao() {
            when(clienteRepository.existeClientePorEmail(anyString())).thenReturn(true);
            assertThrows(RecursoDuplicadoException.class, () -> clienteService.criarCliente(clienteRequest));
        }

        @Test
        @DisplayName("Criar cliente quando CPF já existe deve lançar exceção")
        void criarClienteQuandoCPFJaExisteDeveLancarExcecao() {
            when(clienteRepository.existeClientePorDocumento(anyString())).thenReturn(true);
            assertThrows(RecursoDuplicadoException.class, () -> clienteService.criarCliente(clienteRequest));
        }
    }

    @Nested
    @DisplayName("Testes para encontrar cliente")
    class EncontrarClienteTeste {
        @Test
        @DisplayName("Encontrar cliente por ID quando cliente existe deve retornar cliente")
        void encontrarClientePorIdQuandoClienteExisteDeveRetornarCliente() {
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
            var resultado = clienteService.encontrarClientePorId(cliente.getId());
            assertNotNull(resultado);
            assertThat(resultado).isEqualTo(cliente);
            verify(clienteRepository, times(1)).findById(cliente.getId());
        }

        @Test
        @DisplayName("Encontrar cliente por ID quando não existe deve lançar exceção")
        void encontrarClientePorIdQuandoNaoExisteDeveLancarExcecao() {
            when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThrows(RecursoInvalidoException.class, () -> clienteService.encontrarClientePorId(anyLong()));
            verify(clienteRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("Encontrar cliente por email quando cliente existe deve retornar cliente")
        void encontrarClientePorEmailQuandoClienteExisteDeveRetornarCliente() {
            when(clienteRepository.encontrarClientePorEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));
            var resultado = clienteService.encontrarClientePorEmail(cliente.getEmail());
            assertNotNull(resultado);
            assertThat(cliente).isEqualTo(resultado);
            verify(clienteRepository, times(1)).encontrarClientePorEmail(cliente.getEmail());
        }

        @Test
        @DisplayName("Encontrar cliente por email quando não existe deve lançar exceção")
        void encontrarClientePorEmailQuandoNaoExisteDeveLancarExcecao() {
            when(clienteRepository.encontrarClientePorEmail(anyString())).thenReturn(Optional.empty());
            assertThrows(RecursoInvalidoException.class, () -> clienteService.encontrarClientePorEmail(anyString()));
            verify(clienteRepository, times(1)).encontrarClientePorEmail(anyString());
        }

        @Test
        @DisplayName("Encontrar todos os clientes")
        void encontrarTodosClientes() {
            when(clienteRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(cliente)));
            Page<Cliente> clientes = clienteService.encontrarTodosClientes(PageRequest.of(0, 10));
            assertThat(clientes.getContent()).contains(cliente);
            verify(clienteRepository, times(1)).findAll(any(Pageable.class));
        }

        @Test
        @DisplayName("Encontrar cliente parte do nome")
        void encontrarClienteParteNome() {
            when(clienteRepository.findByNomeContainsIgnoreCase(eq("teste"), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(cliente)));
            Page<Cliente> clientes = clienteService.encontrarClienteParteNome("teste", PageRequest.of(0, 10));
            assertThat(clientes.getContent()).contains(cliente);
            verify(clienteRepository, times(1)).findByNomeContainsIgnoreCase(eq("teste"), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("Testes para atualizarCliente()")
    class AtualizarClienteTeste {
        @Test
        @DisplayName("Atualizar cliente")
        void atualizarCliente() {
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
            when(clienteRepository.existeClientePorDocumento(clienteRequest.getCpf())).thenReturn(false);
            when(clienteRepository.existeClientePorEmail(clienteRequest.getEmail())).thenReturn(false);
            var clienteAtualizado = clienteService.atualizarCliente(cliente.getId(), clienteRequest);
            assertThat(clienteAtualizado).isEqualTo(cliente);
            verify(clienteRepository, times(1)).save(cliente);
        }

        @Test
        @DisplayName("Atualizar cliente quando CPF existente lança exceção")
        void atualizarClienteCpfExistenteLancaExcecao() {
            clienteRequest.setCpf("01234567890");
            when(clienteRepository.existeClientePorDocumento(clienteRequest.getCpf())).thenReturn(true);
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
            assertThrows(RecursoDuplicadoException.class, () -> clienteService.atualizarCliente(cliente.getId(), clienteRequest));
        }

        @Test
        @DisplayName("Atualizar cliente quando Email existente lança exceção")
        void atualizarClienteEmailExistenteLancaExcecao() {
            clienteRequest.setEmail("atualizar@email.com");
            when(clienteRepository.existeClientePorEmail(clienteRequest.getEmail())).thenReturn(true);
            when(clienteRepository.findById(cliente.getId())).thenReturn(Optional.of(cliente));
            assertThrows(RecursoDuplicadoException.class, () -> clienteService.atualizarCliente(cliente.getId(), clienteRequest));
        }
    }
}