package com.hemoplay.dto;

import jakarta.validation.constraints.NotNull;

public record DoacaoRequest(
        @NotNull(message = "Usuário é obrigatório")
        Long idUsuario,

        Long idLocal
) {
}