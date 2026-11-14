package com.hemoplay.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hemoplay.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {
}
