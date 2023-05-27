package dev.borriguel.bancodigital.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.borriguel.bancodigital.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.email = :email")
    boolean existeClientePorEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.cpf = :cpf")
    boolean existeClientePorDocumento(@Param("cpf") String documento);

    @Query("FROM Cliente c WHERE c.email = :email")
    Optional<Cliente> encontrarClientePorEmail(@Param("email") String email);
    Page<Cliente> findByNomeContainsIgnoreCase(String nome, Pageable pageable);

    @Query("select c from Cliente c where c.contaComum.id = :id")
    Cliente encontrarClientePeloIdContaComum(@Param("id") UUID id);
    @Query("select c from Cliente c where c.contaLojista.id = :id")
    Cliente encontrarClientePeloIdContaLojista(@Param("id") UUID id);

}
