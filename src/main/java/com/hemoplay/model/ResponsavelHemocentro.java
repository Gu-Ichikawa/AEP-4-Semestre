package com.hemoplay.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("RESPONSAVEL_HEMOCENTRO")
public class ResponsavelHemocentro extends Usuario {

    @Override
    public List<String> obterPermissoes() {
        return List.of("atualizarLocal", "gerenciarCampanhas");
    }
}