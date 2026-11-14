package com.hemoplay.model.requisito;

public class RequisitoNivel implements RequisitoDesbloqueio {

    private final String nivelExigido;

    public RequisitoNivel(String nivelExigido) {
        this.nivelExigido = nivelExigido;
    }

    @Override
    public boolean estaCumprido(ProgressoUsuario progresso) {
        return progresso.niveisAlcancados().contains(nivelExigido);
    }

    @Override
    public String descricao() {
        return String.format("Alcance o nível %s", nivelExigido);
    }
}
