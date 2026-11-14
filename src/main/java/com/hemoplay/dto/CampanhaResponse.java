package com.hemoplay.dto;

import com.hemoplay.model.Campanha;
import java.time.LocalDate;

public record CampanhaResponse(
        Long id,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        String status,
        String local,
        String cidade,
        String tipoSanguineo
) {

    public static CampanhaResponse from(Campanha campanha) {
        return new CampanhaResponse(
                campanha.getIdCampanha(),
                campanha.getNome(),
                campanha.getDescricao(),
                campanha.getDataInicio(),
                campanha.getDataFim(),
                campanha.getStatus(),
                campanha.getLocal() == null ? null : campanha.getLocal().getNome(),
                campanha.getLocal() == null ? null
                        : campanha.getLocal().getCidade() + ", " + campanha.getLocal().getEstado(),
                campanha.getTipoSanguineo() == null ? null : campanha.getTipoSanguineo().getRotulo()
        );
    }
}
