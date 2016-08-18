package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Tranche.
 */
@Entity
@Table(name = "tranche")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tranche")
public class Tranche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "code", length = 50)
    private String code;

    @Size(max = 200)
    @Column(name = "libelle_fr", length = 200)
    private String libelleFr;

    @Size(max = 200)
    @Column(name = "libelle_en", length = 200)
    private String libelleEn;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "date_deb")
    private LocalDate dateDeb;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @ManyToOne
    private RegimePension regimePension;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelleFr() {
        return libelleFr;
    }

    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    public String getLibelleEn() {
        return libelleEn;
    }

    public void setLibelleEn(String libelleEn) {
        this.libelleEn = libelleEn;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public LocalDate getDateDeb() {
        return dateDeb;
    }

    public void setDateDeb(LocalDate dateDeb) {
        this.dateDeb = dateDeb;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public RegimePension getRegimePension() {
        return regimePension;
    }

    public void setRegimePension(RegimePension regimePension) {
        this.regimePension = regimePension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tranche tranche = (Tranche) o;
        if(tranche.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tranche.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tranche{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelleFr='" + libelleFr + "'" +
            ", libelleEn='" + libelleEn + "'" +
            ", numero='" + numero + "'" +
            ", dateDeb='" + dateDeb + "'" +
            ", dateFin='" + dateFin + "'" +
            '}';
    }
}
