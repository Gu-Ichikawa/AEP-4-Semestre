package com.hemoplay.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UsuarioConquistaId implements Serializable {

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_conquista")
    private Long idConquista;

    protected UsuarioConquistaId() {
    }

    public UsuarioConquistaId(Long idUsuario, Long idConquista) {
        this.idUsuario = idUsuario;
        this.idConquista = idConquista;
    }

    public Long getIdUsuario() { return idUsuario; }
    public Long getIdConquista() { return idConquista; }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (!(outro instanceof UsuarioConquistaId id)) {
            return false;
        }
        return Objects.equals(idUsuario, id.idUsuario) && Objects.equals(idConquista, id.idConquista);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idConquista);
    }
}
