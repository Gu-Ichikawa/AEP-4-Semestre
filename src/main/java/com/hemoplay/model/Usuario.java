package com.hemoplay.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

// Classe abstrata: não pode ser instanciada diretamente (pilar 1 de POO).
// SINGLE_TABLE: todas as subclasses ficam na tabela "usuario".
// A coluna tipo_usuario diz qual subclasse é cada linha.
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // Guarda o hash BCrypt, nunca a senha em texto puro
    @Column(name = "senha", nullable = false, length = 100)
    private String senha;

    @Column(name = "data_nasc")
    private LocalDate dataNasc;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "pontuacao", nullable = false)
    private int pontuacao;

    @Column(name = "mascote_cor", length = 30)
    private String mascoteCor;

    @Column(name = "mascote_acessorios", length = 255)
    private String mascoteAcessorios;

    @ManyToOne
    @JoinColumn(name = "id_tipo_sanguineo")
    private TipoSanguineo tipoSanguineo;

    @ManyToOne
    @JoinColumn(name = "id_nivel")
    private Nivel nivel;

    protected Usuario() {
    }

    // Método abstrato: cada subclasse precisa responder com a sua própria lista (pilar 2 e 3 de POO)
    public abstract List<String> obterPermissoes();

    public Long getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public LocalDate getDataNasc() { return dataNasc; }
    public void setDataNasc(LocalDate dataNasc) { this.dataNasc = dataNasc; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }
    public String getMascoteCor() { return mascoteCor; }
    public void setMascoteCor(String mascoteCor) { this.mascoteCor = mascoteCor; }
    public String getMascoteAcessorios() { return mascoteAcessorios; }
    public void setMascoteAcessorios(String mascoteAcessorios) { this.mascoteAcessorios = mascoteAcessorios; }
    public TipoSanguineo getTipoSanguineo() { return tipoSanguineo; }
    public void setTipoSanguineo(TipoSanguineo tipoSanguineo) { this.tipoSanguineo = tipoSanguineo; }
    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }
}