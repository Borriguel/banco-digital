package dev.borriguel.bancodigital.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.borriguel.bancodigital.entity.Lojista;

@Repository
public interface LojistaRepository extends JpaRepository<Lojista, UUID> {
}
