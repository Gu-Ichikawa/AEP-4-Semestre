package com.hemoplay.repository;

import com.hemoplay.model.Notificacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByIdUsuarioOrderByDataEnvioDesc(Long idUsuario);
}
