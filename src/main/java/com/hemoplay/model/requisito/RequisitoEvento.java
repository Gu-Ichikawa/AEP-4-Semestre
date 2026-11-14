package com.hemoplay.model.requisito;

public class RequisitoEvento implements RequisitoDesbloqueio {

    private final String eventoExigido;

    public RequisitoEvento(String eventoExigido) {
        this.eventoExigido = eventoExigido;
    }

    @Override
    public boolean estaCumprido(ProgressoUsuario progresso) {
        return progresso.eventosAcontecendo().contains(eventoExigido);
    }

    @Override
    public String descricao() {
        return String.format("Disponível durante o evento %s", eventoExigido);
    }
}
