package dev.borriguel.bancodigital.repository;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.borriguel.bancodigital.entity.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("FROM Transacao tran WHERE tran.data BETWEEN :min AND :max")
    Page<Transacao> encontrarTransacoes(LocalDate min, LocalDate max, Pageable page);
}
