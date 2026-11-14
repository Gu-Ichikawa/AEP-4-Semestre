package com.hemoplay.model.requisito;

public class RequisitoMissao implements RequisitoDesbloqueio {

    private final String missaoExigida;

    public RequisitoMissao(String missaoExigida) {
        this.missaoExigida = missaoExigida;
    }

    @Override
    public boolean estaCumprido(ProgressoUsuario progresso) {
        return progresso.missoesConcluidas().contains(missaoExigida);
    }

    @Override
    public String descricao() {
        return String.format("Conclua a missão %s", missaoExigida);
    }
}
