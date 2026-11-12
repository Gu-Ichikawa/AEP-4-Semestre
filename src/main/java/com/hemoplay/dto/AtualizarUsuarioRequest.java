package com.hemoplay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AtualizarUsuarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120)
        String nome,

        @Size(max = 20)
        String telefone,

        LocalDate dataNasc,

        Long idTipoSanguineo
) {
}