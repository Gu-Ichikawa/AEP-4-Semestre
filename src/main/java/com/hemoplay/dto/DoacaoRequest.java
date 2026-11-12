package com.hemoplay.dto;

import jakarta.validation.constraints.NotNull;

public record DoacaoRequest(
        @NotNull(message = "Usuário é obrigatório")
        Long idUsuario,

        // Opcional: a doação pode não ter local definido
        Long idLocal
) {
}