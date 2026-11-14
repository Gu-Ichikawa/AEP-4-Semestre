package com.hemoplay.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_item", nullable = false)
    private Long idItem;

    @Column(name = "data_desbloqueio", nullable = false)
    private LocalDateTime dataDesbloqueio;

    @Column(name = "equipado", nullable = false)
    private Boolean equipado;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    protected Inventario() {
    }

    public Inventario(Long idUsuario, Long idItem) {
        this.idUsuario = idUsuario;
        this.idItem = idItem;
        this.dataDesbloqueio = LocalDateTime.now();
        this.equipado = false;
        this.quantidade = 1;
    }

    public void equipar() {
        this.equipado = true;
    }

    public void desequipar() {
        this.equipado = false;
    }

    public Long getIdInventario() { return idInventario; }
    public Long getIdUsuario() { return idUsuario; }
    public Long getIdItem() { return idItem; }
    public LocalDateTime getDataDesbloqueio() { return dataDesbloqueio; }
    public Boolean getEquipado() { return equipado; }
    public Integer getQuantidade() { return quantidade; }
}
