package com.hemoplay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UsuarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120)
        String nome,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 150)
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 72, message = "Senha deve ter entre 6 e 72 caracteres")
        String senha,

        LocalDate dataNasc,

        @Size(max = 20)
        String telefone,

        Long idTipoSanguineo
) {
}