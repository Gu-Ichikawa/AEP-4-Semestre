package com.hemoplay.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "campanha")
public class Campanha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campanha")
    private Long idCampanha;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_local")
    private LocalDoacao local;

    @ManyToOne
    @JoinColumn(name = "id_tipo_sanguineo")
    private TipoSanguineo tipoSanguineo;

    protected Campanha() {
    }

    public Long getIdCampanha() { return idCampanha; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public String getStatus() { return status; }
    public LocalDoacao getLocal() { return local; }
    public TipoSanguineo getTipoSanguineo() { return tipoSanguineo; }
}
