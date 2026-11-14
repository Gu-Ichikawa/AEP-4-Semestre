package com.hemoplay.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificacao")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacao")
    private Long idNotificacao;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "mensagem", nullable = false, length = 500)
    private String mensagem;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;

    @Column(name = "lida", nullable = false)
    private Boolean lida;

    protected Notificacao() {
    }

    public Notificacao(Long idUsuario, String tipo, String titulo, String mensagem) {
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataEnvio = LocalDateTime.now();
        this.lida = false;
    }

    public void marcarComoLida() {
        this.lida = true;
    }

    public Long getIdNotificacao() { return idNotificacao; }
    public Long getIdUsuario() { return idUsuario; }
    public String getTitulo() { return titulo; }
    public String getMensagem() { return mensagem; }
    public String getTipo() { return tipo; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public Boolean getLida() { return lida; }
}
