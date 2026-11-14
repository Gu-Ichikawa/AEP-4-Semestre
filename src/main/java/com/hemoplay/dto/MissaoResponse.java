package com.hemoplay.dto;

import com.hemoplay.model.Missao;
import com.hemoplay.model.ParticipacaoMissao;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MissaoResponse(
        Long id,
        String nome,
        String descricao,
        String tipo,
        int pontuacao,
        LocalDate dataFim,
        boolean doEvento,
        String situacao,
        LocalDateTime dataConclusao,
        boolean concluidaAoVisitar
) {

    public static MissaoResponse from(Missao missao, ParticipacaoMissao participacao, LocalDate hoje) {
        String situacao;
        if (participacao != null) {
            situacao = "CONCLUIDA";
        } else if (missao.estaDisponivel(hoje)) {
            situacao = "DISPONIVEL";
        } else {
            situacao = "ENCERRADA";
        }
        return new MissaoResponse(
                missao.getIdMissao(),
                missao.getNome(),
                missao.getDescricao(),
                missao.getTipo(),
                missao.getPontuacao(),
                missao.getDataFim(),
                missao.getIdEvento() != null,
                situacao,
                participacao == null ? null : participacao.getDataConclusao(),
                missao.concluidaAoVisitar()
        );
    }
}
