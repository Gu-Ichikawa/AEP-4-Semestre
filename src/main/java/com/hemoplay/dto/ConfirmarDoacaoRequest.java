package com.hemoplay.dto;

import jakarta.validation.constraints.NotNull;

// Sem login nesta entrega, o administrador informa o próprio id.
// Com autenticação (fora do escopo), esse id viria do usuário logado.
public record ConfirmarDoacaoRequest(
        @NotNull(message = "Administrador é obrigatório")
        Long idAdministrador
) {
}