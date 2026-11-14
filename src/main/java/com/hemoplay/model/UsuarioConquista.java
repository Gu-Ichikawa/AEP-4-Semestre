package com.hemoplay.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario_conquista")
public class UsuarioConquista {

    @EmbeddedId
    private UsuarioConquistaId id;

    @Column(name = "data_desbloqueio", nullable = false)
    private LocalDateTime dataDesbloqueio;

    protected UsuarioConquista() {
    }

    public UsuarioConquista(Long idUsuario, Long idConquista) {
        this.id = new UsuarioConquistaId(idUsuario, idConquista);
        this.dataDesbloqueio = LocalDateTime.now();
    }

    public UsuarioConquistaId getId() { return id; }
    public LocalDateTime getDataDesbloqueio() { return dataDesbloqueio; }
}
