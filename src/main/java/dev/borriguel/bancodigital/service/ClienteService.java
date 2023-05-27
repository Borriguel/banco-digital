package dev.borriguel.bancodigital.service;

import dev.borriguel.bancodigital.controller.dto.ClienteDTO;
import dev.borriguel.bancodigital.controller.dto.ClientePost;
import dev.borriguel.bancodigital.exception.custom.RecursoInvalidoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    /**
     * Cria um novo cliente no banco de dados.
     *
     * @param clientePost a classe modelo com as informações do cliente a ser criado.
     * @return o objeto {@link ClienteDTO} com as informações do cliente criado.
     */
    ClienteDTO criarCliente(ClientePost clientePost);

    /**
     * Busca um cliente no banco de dados pelo seu ID.
     *
     * @param id o ID do cliente a ser buscado.
     * @return o objeto {@link ClienteDTO} com as informações do cliente buscado.
     * @throws RecursoInvalidoException se o cliente com o ID especificado não for encontrado.
     */
    ClienteDTO encontrarClientePorId(Long id) throws RecursoInvalidoException;

    /**
     * Busca um cliente no banco de dados pelo seu e-mail.
     *
     * @param email o e-mail do cliente a ser buscado.
     * @return o objeto {@link ClienteDTO} com as informações do cliente buscado.
     * @throws RecursoInvalidoException se o cliente com o e-mail especificado não for encontrado.
     */
    ClienteDTO encontrarClientePorEmail(String email) throws RecursoInvalidoException;

    /**
     * Busca todos os clientes no banco de dados e retorna uma página com eles.
     *
     * @param page informações sobre a página que se deseja buscar.
     * @return a página com os DTOs dos clientes buscados.
     */
    Page<ClienteDTO> encontrarTodosClientes(Pageable page);

    /**
     * Atualiza as informações de um cliente no banco de dados.
     *
     * @param id o ID do cliente a ser atualizado.
     * @param clientePost a classe modelo com as informações atualizadas do cliente.
     * @return o objeto {@link ClienteDTO} com as informações do cliente atualizado.
     * @throws RecursoInvalidoException se o cliente com o ID especificado não for encontrado.
     */
    ClienteDTO atualizarCliente(Long id, ClientePost clientePost) throws RecursoInvalidoException;

    /**
     * Deleta um cliente do banco de dados.
     *
     * @param id o ID do cliente a ser deletado.
     * @throws RecursoInvalidoException se o cliente com o ID especificado não for encontrado.
     */
    void deletarCliente(Long id) throws RecursoInvalidoException;

    /**
     * Busca clientes no banco de dados cujo nome contenha uma determinada substring.
     *
     * @param nome a substring que se deseja buscar no nome dos clientes.
     * @param page informações sobre a página que se deseja buscar.
     * @return a página com os DTOs dos clientes buscados.
     */
    Page<ClienteDTO> encontrarClienteParteNome(String nome, Pageable page);
}
