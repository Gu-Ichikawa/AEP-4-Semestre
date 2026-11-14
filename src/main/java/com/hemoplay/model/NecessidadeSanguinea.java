package com.hemoplay.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "necessidade_sanguinea")
public class NecessidadeSanguinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_necessidade")
    private Long idNecessidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo_sanguineo", nullable = false)
    private TipoSanguineo tipoSanguineo;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "quantidade_simulada", nullable = false)
    private Integer quantidadeSimulada;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    protected NecessidadeSanguinea() {
    }

    public Long getIdNecessidade() { return idNecessidade; }
    public TipoSanguineo getTipoSanguineo() { return tipoSanguineo; }
    public String getStatus() { return status; }
    public Integer getQuantidadeSimulada() { return quantidadeSimulada; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
}
