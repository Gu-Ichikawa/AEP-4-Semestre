package com.hemoplay.dto;

import jakarta.validation.constraints.NotNull;

public record ConfirmarDoacaoRequest(
        @NotNull(message = "Usuário que confirma é obrigatório")
        Long idUsuarioConfirmacao
) {
}