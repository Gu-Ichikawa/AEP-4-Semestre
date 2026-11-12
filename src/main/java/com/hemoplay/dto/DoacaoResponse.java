package com.hemoplay.dto;

import com.hemoplay.model.Doacao;
import java.time.LocalDateTime;

public record DoacaoResponse(
        Long id,
        String status,
        LocalDateTime dataRegistro,
        LocalDateTime dataConfirmacao,
        String local
) {

    public static DoacaoResponse from(Doacao doacao) {
        return new DoacaoResponse(
                doacao.getIdDoacao(),
                doacao.getStatus().name(),
                doacao.getDataRegistro(),
                doacao.getDataConfirmacao(),
                doacao.getLocal() == null ? null : doacao.getLocal().getNome()
        );
    }
}