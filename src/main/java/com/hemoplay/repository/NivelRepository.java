package com.hemoplay.repository;

import com.hemoplay.model.Nivel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NivelRepository extends JpaRepository<Nivel, Long> {

    Optional<Nivel> findByNome(String nome);
}