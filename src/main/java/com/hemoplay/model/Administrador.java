package com.hemoplay.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario {

    @Override
    public List<String> obterPermissoes() {
        return List.of("confirmarDoacao", "gerenciarUsuarios", "gerenciarCampanhas", "gerenciarMissoes");
    }
}