package dev.borriguel.bancodigital.controller;

import dev.borriguel.bancodigital.controller.dto.ClienteRequest;
import dev.borriguel.bancodigital.controller.dto.ClienteResponse;
import dev.borriguel.bancodigital.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
@Tag(name = "Cliente", description = "Endpoints do controlador cliente.")
public class ClienteController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteService service;
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Cria um novo cliente.")
    public ResponseEntity<ClienteResponse> criarCliente(@Valid @RequestBody ClienteRequest clienteRequest) {
        var cliente = service.criarCliente(clienteRequest);
        logger.info("Cliente criado -> {}", cliente);
        var clienteResponse = modelMapper.map(cliente, ClienteResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Encontra um cliente pelo seu id.")
    public ResponseEntity<ClienteResponse> encontrarCliente(@PathVariable Long id) {
        var cliente = service.encontrarClientePorId(id);
        logger.info("Cliente com id {} encontrado -> {}", id, cliente);
        var clienteResponse = modelMapper.map(cliente, ClienteResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponse);
    }

    @GetMapping("/")
    @Operation(summary = "Encontra um cliente pelo email.")
    public ResponseEntity<ClienteResponse> encontrarClienteEmail(@RequestParam String email) {
        var cliente = service.encontrarClientePorEmail(email);
        logger.info("Cliente encontrado -> {}", cliente);
        var clienteResponse = modelMapper.map(cliente, ClienteResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponse);
    }

    @GetMapping
    @Operation(summary = "Encontra uma lista paginada de cliente que contenham no nome a substring informada.")
    public ResponseEntity<Page<ClienteResponse>> encontrarClientesParteNome(@RequestParam String nome, @ParameterObject Pageable page) {
        var clientesPaged = service.encontrarClienteParteNome(nome, page);
        var clientesResponsePaged = clientesPaged.map((cliente) -> modelMapper.map(cliente, ClienteResponse.class));
        return ResponseEntity.status(HttpStatus.OK).body(clientesResponsePaged);
    }

    @GetMapping("/todos")
    @Operation(summary = "Encontra todos os clientes em uma lista paginada.")
    public ResponseEntity<Page<ClienteResponse>> encontrarTodosClientes(
            @PageableDefault(sort = "id") @ParameterObject Pageable page) {
        var clientesPaged = service.encontrarTodosClientes(page);
        var clientesResponsePaged = clientesPaged.map((cliente) -> modelMapper.map(cliente, ClienteResponse.class));
        return ResponseEntity.status(HttpStatus.OK).body(clientesResponsePaged);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um cliente pelo seu id.")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        service.deletarCliente(id);
        logger.info("Cliente deletado com id -> {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados do cliente pelo seu id.")
    public ResponseEntity<ClienteResponse> atualizarCliente(@PathVariable Long id,
                                                            @Valid @RequestBody ClienteRequest clienteRequest) {
        var clienteAtualizado = service.atualizarCliente(id, clienteRequest);
        logger.info("Cliente com id {} atualizado -> {}", id, clienteAtualizado);
        var clienteResponse = modelMapper.map(clienteAtualizado, ClienteResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponse);
    }
}
