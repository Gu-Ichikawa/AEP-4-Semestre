package com.hemoplay.repository;

import com.hemoplay.model.Doacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    // Spring cria a consulta a partir do nome: usuario.idUsuario, ordenado por data
    List<Doacao> findByUsuarioIdUsuarioOrderByDataRegistroDesc(Long idUsuario);
}