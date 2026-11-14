package com.hemoplay.model.requisito;

public class SemRequisito implements RequisitoDesbloqueio {

    @Override
    public boolean estaCumprido(ProgressoUsuario progresso) {
        return true;
    }

    @Override
    public String descricao() {
        return "Liberado desde o início";
    }
}
