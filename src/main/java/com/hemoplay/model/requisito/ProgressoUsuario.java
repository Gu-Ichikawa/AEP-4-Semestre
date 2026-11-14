package com.hemoplay.model.requisito;

import java.util.Set;

public record ProgressoUsuario(
        Set<String> niveisAlcancados,
        Set<String> missoesConcluidas,
        Set<String> conquistasDesbloqueadas,
        Set<String> eventosAcontecendo
) {
}
