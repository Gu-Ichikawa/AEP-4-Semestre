package com.hemoplay.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tipo_sanguineo")
public class TipoSanguineo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_sanguineo")
    private Long idTipoSanguineo;

    @Column(name = "tipo", nullable = false, length = 2)
    private String tipo;

    // fator_rh é CHAR(1) no banco. Essa anotação avisa o Hibernate para não reclamar do tipo.
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "fator_rh", nullable = false, length = 1)
    private String fatorRh;

    protected TipoSanguineo() {
    }

    public Long getIdTipoSanguineo() { return idTipoSanguineo; }
    public String getTipo() { return tipo; }
    public String getFatorRh() { return fatorRh; }

    public String getRotulo() {
        return tipo + fatorRh; // exemplo: "O+"
    }
}