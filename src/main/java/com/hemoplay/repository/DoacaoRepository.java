package com.hemoplay.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hemoplay.model.Doacao;
import com.hemoplay.model.StatusDoacao;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    List<Doacao> findByUsuarioIdUsuarioOrderByDataRegistroDesc(Long idUsuario);

    List<Doacao> findByUsuarioIdUsuarioAndStatus(Long idUsuario, StatusDoacao status);

    List<Doacao> findByStatusOrderByDataRegistroAsc(StatusDoacao status);
}