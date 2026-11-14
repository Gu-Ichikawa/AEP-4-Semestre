package com.hemoplay.repository;

import com.hemoplay.model.ParticipacaoMissao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, Long> {

    List<ParticipacaoMissao> findByIdUsuario(Long idUsuario);

    boolean existsByIdUsuarioAndIdMissao(Long idUsuario, Long idMissao);
}
