package com.hemoplay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hemoplay.model.Campanha;

public interface CampanhaRepository extends JpaRepository<Campanha, Long> {

    List<Campanha> findAllByOrderByDataInicioAsc();
}
