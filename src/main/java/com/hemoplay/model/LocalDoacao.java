package com.hemoplay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "local_doacao")
public class LocalDoacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local")
    private Long idLocal;

    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "endereco", nullable = false, length = 100)
    private String endereco;

    @Column(name = "cidade", nullable = false, length = 30)
    private String cidade;

    @Column(name = "horario_funcionamento", length = 100)
    private String horarioFuncionamento;

    @Column(name = "descricao", length = 500)
    private String descricao;

    protected LocalDoacao() {
    }

    public Long getIdLocal() { return idLocal; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEstado() { return estado; }
    public String getEndereco() { return endereco; }
    public String getCidade() { return cidade; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public String getDescricao() { return descricao; }
}