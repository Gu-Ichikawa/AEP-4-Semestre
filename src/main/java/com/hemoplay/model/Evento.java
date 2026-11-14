package com.hemoplay.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idEvento;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "tipo", length = 30)
    private String tipo;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    protected Evento() {
    }

    public boolean estaAcontecendo(LocalDate data) {
        return Boolean.TRUE.equals(ativo) && !data.isBefore(dataInicio) && !data.isAfter(dataFim);
    }

    public Long getIdEvento() { return idEvento; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public String getTipo() { return tipo; }
    public Boolean getAtivo() { return ativo; }
}
