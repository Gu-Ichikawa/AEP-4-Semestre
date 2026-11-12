package com.hemoplay.repository;

import com.hemoplay.model.TipoSanguineo;
import org.springframework.data.jpa.repository.JpaRepository;

// findById já vem do JpaRepository, então não precisa de método extra
public interface TipoSanguineoRepository extends JpaRepository<TipoSanguineo, Long> {
}