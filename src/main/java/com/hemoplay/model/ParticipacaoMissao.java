package com.hemoplay.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "participacao_missao")
public class ParticipacaoMissao {

    public static final String CONCLUIDA = "CONCLUIDA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participacao")
    private Long idParticipacao;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_missao", nullable = false)
    private Long idMissao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "recompensa_recebida", nullable = false)
    private Boolean recompensaRecebida;

    protected ParticipacaoMissao() {
    }

    public ParticipacaoMissao(Long idUsuario, Long idMissao) {
        LocalDateTime agora = LocalDateTime.now();
        this.idUsuario = idUsuario;
        this.idMissao = idMissao;
        this.dataInicio = agora;
        this.dataConclusao = agora;
        this.status = CONCLUIDA;
        this.recompensaRecebida = true;
    }

    public boolean estaConcluida() {
        return CONCLUIDA.equals(status);
    }

    public Long getIdParticipacao() { return idParticipacao; }
    public Long getIdUsuario() { return idUsuario; }
    public Long getIdMissao() { return idMissao; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public String getStatus() { return status; }
    public Boolean getRecompensaRecebida() { return recompensaRecebida; }
}
