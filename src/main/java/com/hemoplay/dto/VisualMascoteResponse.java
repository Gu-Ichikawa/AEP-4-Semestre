package com.hemoplay.dto;

import com.hemoplay.model.Item;

public record VisualMascoteResponse(
        Long id,
        String nome,
        String descricao,
        String tipo,
        String imagem,
        String requisito,
        boolean desbloqueado,
        boolean equipado
) {

    public static VisualMascoteResponse from(Item item, boolean desbloqueado, boolean equipado) {
        return new VisualMascoteResponse(
                item.getIdItem(),
                item.getNome(),
                item.getDescricao(),
                item.getTipo(),
                item.getImagem(),
                item.obterRequisito().descricao(),
                desbloqueado,
                equipado
        );
    }
}
