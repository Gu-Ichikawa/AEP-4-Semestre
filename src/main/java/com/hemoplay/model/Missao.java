package com.hemoplay.model;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "missao")
public class Missao {

    public static final Set<String> MISSOES_DE_VISITA = Set.of(
            "Conhecer os requisitos", "Conhecer o intervalo", "Visitar um local", "Ver seu histórico");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_missao")
    private Long idMissao;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "criterio", length = 255)
    private String criterio;

    @Column(name = "pontuacao", nullable = false)
    private int pontuacao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa;

    @Column(name = "id_evento")
    private Long idEvento;

    protected Missao() {
    }

    public boolean estaDisponivel(LocalDate data) {
        boolean depoisDoInicio = dataInicio == null || !data.isBefore(dataInicio);
        boolean antesDoFim = dataFim == null || !data.isAfter(dataFim);
        return Boolean.TRUE.equals(ativa) && depoisDoInicio && antesDoFim;
    }

    public boolean concluidaAoVisitar() {
        return MISSOES_DE_VISITA.contains(nome);
    }

    public Long getIdMissao() { return idMissao; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getTipo() { return tipo; }
    public String getCriterio() { return criterio; }
    public int getPontuacao() { return pontuacao; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public Boolean getAtiva() { return ativa; }
    public Long getIdEvento() { return idEvento; }
}
