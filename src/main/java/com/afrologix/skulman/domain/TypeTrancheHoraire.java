package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A TypeTrancheHoraire.
 */
@Entity
@Table(name = "type_tranche_horaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "typetranchehoraire")
public class TypeTrancheHoraire implements Serializable {

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

    @Column(name = "heure_deb")
    private ZonedDateTime heureDeb;

    @Column(name = "heure_fin")
    private ZonedDateTime heureFin;

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

    public ZonedDateTime getHeureDeb() {
        return heureDeb;
    }

    public void setHeureDeb(ZonedDateTime heureDeb) {
        this.heureDeb = heureDeb;
    }

    public ZonedDateTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(ZonedDateTime heureFin) {
        this.heureFin = heureFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeTrancheHoraire typeTrancheHoraire = (TypeTrancheHoraire) o;
        if(typeTrancheHoraire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, typeTrancheHoraire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TypeTrancheHoraire{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelleFr='" + libelleFr + "'" +
            ", libelleEn='" + libelleEn + "'" +
            ", heureDeb='" + heureDeb + "'" +
            ", heureFin='" + heureFin + "'" +
            '}';
    }
}
