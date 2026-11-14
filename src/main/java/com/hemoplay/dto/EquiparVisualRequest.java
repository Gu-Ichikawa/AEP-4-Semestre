package com.hemoplay.dto;

import jakarta.validation.constraints.NotNull;

public record EquiparVisualRequest(
        @NotNull(message = "Escolha um visual")
        Long idItem
) {
}
