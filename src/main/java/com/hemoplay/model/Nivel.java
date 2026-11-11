package com.hemoplay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "nivel")
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    private Long idNivel;

    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "pontuacao_min", nullable = false)
    private int pontuacaoMin;

    @Column(name = "pontuacao_max")
    private Integer pontuacaoMax;

    protected Nivel() {
    }

    public Long getIdNivel() { return idNivel; }
    public String getNome() { return nome; }
    public int getPontuacaoMin() { return pontuacaoMin; }
    public Integer getPontuacaoMax() { return pontuacaoMax; }
}