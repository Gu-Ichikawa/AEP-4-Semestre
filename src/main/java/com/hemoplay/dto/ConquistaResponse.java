package com.hemoplay.dto;

import com.hemoplay.model.Conquista;
import com.hemoplay.model.UsuarioConquista;
import java.time.LocalDateTime;

public record ConquistaResponse(
        Long id,
        String nome,
        String descricao,
        String criterio,
        String recompensa,
        boolean desbloqueada,
        LocalDateTime dataDesbloqueio
) {

    public static ConquistaResponse from(Conquista conquista, UsuarioConquista registro) {
        return new ConquistaResponse(
                conquista.getIdConquista(),
                conquista.getNome(),
                conquista.getDescricao(),
                conquista.getCriterio(),
                conquista.getRecompensa(),
                registro != null,
                registro == null ? null : registro.getDataDesbloqueio()
        );
    }
}
