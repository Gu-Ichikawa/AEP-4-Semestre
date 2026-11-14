package com.hemoplay.dto;

import com.hemoplay.model.Notificacao;
import java.time.LocalDateTime;

public record NotificacaoResponse(
        Long id,
        String tipo,
        String titulo,
        String mensagem,
        LocalDateTime dataEnvio,
        boolean lida
) {

    public static NotificacaoResponse from(Notificacao notificacao) {
        return new NotificacaoResponse(
                notificacao.getIdNotificacao(),
                notificacao.getTipo(),
                notificacao.getTitulo(),
                notificacao.getMensagem(),
                notificacao.getDataEnvio(),
                Boolean.TRUE.equals(notificacao.getLida())
        );
    }
}
