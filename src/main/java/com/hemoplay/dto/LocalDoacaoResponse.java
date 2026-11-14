package com.hemoplay.dto;

import com.hemoplay.model.LocalDoacao;

public record LocalDoacaoResponse(
        Long id,
        String nome,
        String endereco,
        String cidade,
        String estado,
        String telefone,
        String horarioFuncionamento,
        String descricao
) {

    public static LocalDoacaoResponse from(LocalDoacao local) {
        return new LocalDoacaoResponse(
                local.getIdLocal(),
                local.getNome(),
                local.getEndereco(),
                local.getCidade(),
                local.getEstado(),
                local.getTelefone(),
                local.getHorarioFuncionamento(),
                local.getDescricao()
        );
    }
}
