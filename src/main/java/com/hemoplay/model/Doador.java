package com.hemoplay.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue("DOADOR")
public class Doador extends Usuario {

    @Override
    public List<String> obterPermissoes() {
        return List.of("registrarDoacao", "consultarHistorico", "personalizarMascote");
    }
}