package com.hemoplay.repository;

import com.hemoplay.model.Missao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissaoRepository extends JpaRepository<Missao, Long> {

    Optional<Missao> findByNome(String nome);

    List<Missao> findAllByOrderByIdMissaoAsc();
}
