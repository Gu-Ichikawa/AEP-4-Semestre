package com.hemoplay.dto;

import com.hemoplay.model.Evento;
import java.time.LocalDate;

public record EventoResponse(
        Long id,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim
) {

    public static EventoResponse from(Evento evento) {
        return new EventoResponse(
                evento.getIdEvento(),
                evento.getNome(),
                evento.getDescricao(),
                evento.getDataInicio(),
                evento.getDataFim()
        );
    }
}
