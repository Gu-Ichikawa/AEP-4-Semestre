package com.hemoplay.dto;

import com.hemoplay.model.TipoSanguineo;

public record TipoSanguineoResponse(
        Long id,
        String rotulo
) {

    public static TipoSanguineoResponse from(TipoSanguineo tipo) {
        return new TipoSanguineoResponse(tipo.getIdTipoSanguineo(), tipo.getRotulo());
    }
}
