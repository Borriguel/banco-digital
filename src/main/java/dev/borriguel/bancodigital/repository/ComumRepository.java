package dev.borriguel.bancodigital.repository;

import dev.borriguel.bancodigital.entity.Comum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ComumRepository extends JpaRepository<Comum, UUID> {
}
