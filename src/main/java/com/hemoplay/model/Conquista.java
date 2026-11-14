package com.hemoplay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "conquista")
public class Conquista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conquista")
    private Long idConquista;

    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "criterio", nullable = false, length = 255)
    private String criterio;

    @Column(name = "recompensa", length = 255)
    private String recompensa;

    protected Conquista() {
    }

    public Long getIdConquista() { return idConquista; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCriterio() { return criterio; }
    public String getRecompensa() { return recompensa; }
}
