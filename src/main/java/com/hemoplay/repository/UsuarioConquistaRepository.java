package com.hemoplay.repository;

import com.hemoplay.model.UsuarioConquista;
import com.hemoplay.model.UsuarioConquistaId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioConquistaRepository extends JpaRepository<UsuarioConquista, UsuarioConquistaId> {

    @Query("select uc from UsuarioConquista uc where uc.id.idUsuario = :idUsuario")
    List<UsuarioConquista> listarPorUsuario(@Param("idUsuario") Long idUsuario);
}
