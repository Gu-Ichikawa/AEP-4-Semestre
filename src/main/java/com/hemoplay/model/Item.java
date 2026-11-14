package com.hemoplay.model;

import com.hemoplay.model.requisito.RequisitoDesbloqueio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "item")
public class Item {

    public static final String COR = "COR";
    public static final String ACESSORIO = "ACESSORIO";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long idItem;

    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @Column(name = "imagem", length = 255)
    private String imagem;

    @Column(name = "requisito_desbloqueio", length = 255)
    private String requisitoDesbloqueio;

    protected Item() {
    }

    public RequisitoDesbloqueio obterRequisito() {
        return RequisitoDesbloqueio.de(requisitoDesbloqueio);
    }

    public boolean ehCor() {
        return COR.equals(tipo);
    }

    public Long getIdItem() { return idItem; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getTipo() { return tipo; }
    public String getImagem() { return imagem; }
    public String getRequisitoDesbloqueio() { return requisitoDesbloqueio; }
}
