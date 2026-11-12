package com.hemoplay.dto;

import jakarta.validation.constraints.NotNull;

// Sem login nesta entrega, quem confirma (Administrador ou Responsável pelo Hemocentro)
// informa o próprio id.
// Com autenticação (fora do escopo), esse id viria do usuário logado.
public record ConfirmarDoacaoRequest(
        @NotNull(message = "Usuário que confirma é obrigatório")
        Long idUsuarioConfirmacao
) {
}