package com.hemoplay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "doacao")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doacao")
    private Long idDoacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_local")
    private LocalDoacao local;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "status", nullable = false, length = 20)
    private StatusDoacao status;

    protected Doacao() {
    }

    public Doacao(Usuario usuario, LocalDoacao local) {
        this.usuario = usuario;
        this.local = local;
        this.dataRegistro = LocalDateTime.now();
        this.status = StatusDoacao.REGISTRADA;
    }

    public void confirmar(LocalDateTime momento) {
        if (this.status == StatusDoacao.CONFIRMADA) {
            throw new IllegalStateException("Esta doação já foi confirmada.");
        }
        this.status = StatusDoacao.CONFIRMADA;
        this.dataConfirmacao = momento;
    }

    public Long getIdDoacao() { return idDoacao; }
    public Usuario getUsuario() { return usuario; }
    public LocalDoacao getLocal() { return local; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public LocalDateTime getDataConfirmacao() { return dataConfirmacao; }
    public StatusDoacao getStatus() { return status; }
}