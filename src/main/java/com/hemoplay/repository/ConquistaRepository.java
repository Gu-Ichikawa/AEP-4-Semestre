package com.hemoplay.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hemoplay.model.Conquista;

public interface ConquistaRepository extends JpaRepository<Conquista, Long> {

    Optional<Conquista> findByNome(String nome);

    List<Conquista> findAllByOrderByIdConquistaAsc();
}
