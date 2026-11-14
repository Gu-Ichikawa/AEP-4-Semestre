package com.hemoplay.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hemoplay.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByIdUsuario(Long idUsuario);

    Optional<Inventario> findByIdUsuarioAndIdItem(Long idUsuario, Long idItem);
}
