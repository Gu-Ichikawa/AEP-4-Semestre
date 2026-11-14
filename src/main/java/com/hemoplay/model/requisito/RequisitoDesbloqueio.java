package com.hemoplay.model.requisito;

public interface RequisitoDesbloqueio {

    boolean estaCumprido(ProgressoUsuario progresso);

    String descricao();

    static RequisitoDesbloqueio de(String texto) {
        if (texto == null || texto.isBlank()) {
            return new SemRequisito();
        }
        String valor = texto.trim();
        if (valor.startsWith("Nível ")) {
            return new RequisitoNivel(valor.substring("Nível ".length()));
        }
        if (valor.startsWith("Missão ")) {
            return new RequisitoMissao(valor.substring("Missão ".length()));
        }
        if (valor.startsWith("Conquista ")) {
            return new RequisitoConquista(valor.substring("Conquista ".length()));
        }
        if (valor.startsWith("Evento ")) {
            return new RequisitoEvento(valor.substring("Evento ".length()));
        }
        return new SemRequisito();
    }
}
