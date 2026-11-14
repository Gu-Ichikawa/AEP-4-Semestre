package com.hemoplay.model.requisito;

public class RequisitoConquista implements RequisitoDesbloqueio {

    private final String conquistaExigida;

    public RequisitoConquista(String conquistaExigida) {
        this.conquistaExigida = conquistaExigida;
    }

    @Override
    public boolean estaCumprido(ProgressoUsuario progresso) {
        return progresso.conquistasDesbloqueadas().contains(conquistaExigida);
    }

    @Override
    public String descricao() {
        return String.format("Desbloqueie a conquista %s", conquistaExigida);
    }
}
