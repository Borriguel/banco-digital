package dev.borriguel.bancodigital.controller;

import dev.borriguel.bancodigital.controller.dto.ClienteDTO;
import dev.borriguel.bancodigital.controller.dto.ClientePost;
import dev.borriguel.bancodigital.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    private final ClienteService service;

    @PostMapping
    @Operation(summary = "Cria um novo cliente.")
    public ResponseEntity<ClienteDTO> criarCliente(@Valid @RequestBody ClientePost clientePost) {
        var cliente = service.criarCliente(clientePost);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Encontra um cliente pelo seu id.")
    public ResponseEntity<ClienteDTO> encontrarCliente(@PathVariable Long id) {
        var cliente = service.encontrarClientePorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @GetMapping("/")
    @Operation(summary = "Encontra um cliente pelo email.")
    public ResponseEntity<ClienteDTO> encontrarClienteEmail(@RequestParam String email) {
        var cliente = service.encontrarClientePorEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);
    }

    @GetMapping
    @Operation(summary = "Encontra uma lista paginada de cliente que contenham no nome a substring informada.")
    public ResponseEntity<Page<ClienteDTO>> encontrarClientesParteNome(@RequestParam String nome, @ParameterObject Pageable page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.encontrarClienteParteNome(nome, page));
    }

    @GetMapping("/todos")
    @Operation(summary = "Encontra todos os clientes em uma lista paginada.")
    public ResponseEntity<Page<ClienteDTO>> encontrarTodosClientes(
            @PageableDefault(size = 10, sort = "id") @ParameterObject Pageable page) {
        return ResponseEntity.status(HttpStatus.OK).body(service.encontrarTodosClientes(page));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um cliente pelo seu id.")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        service.deletarCliente(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados do cliente pelo seu id.")
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable Long id,
            @Valid @RequestBody ClientePost clienteDTO) {
        var clienteAtualizado = service.atualizarCliente(id, clienteDTO);
        return ResponseEntity.status(HttpStatus.OK).body(clienteAtualizado);
    }
}
