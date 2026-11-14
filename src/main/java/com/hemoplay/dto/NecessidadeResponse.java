package com.hemoplay.dto;

import com.hemoplay.model.NecessidadeSanguinea;
import java.time.LocalDateTime;

public record NecessidadeResponse(
        String tipoSanguineo,
        String status,
        Integer quantidade,
        LocalDateTime dataAtualizacao
) {

    public static NecessidadeResponse from(NecessidadeSanguinea necessidade) {
        return new NecessidadeResponse(
                necessidade.getTipoSanguineo().getRotulo(),
                necessidade.getStatus(),
                necessidade.getQuantidadeSimulada(),
                necessidade.getDataAtualizacao()
        );
    }
}
