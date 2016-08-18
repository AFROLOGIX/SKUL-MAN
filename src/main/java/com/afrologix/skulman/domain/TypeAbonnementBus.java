package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TypeAbonnementBus.
 */
@Entity
@Table(name = "type_abonnement_bus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "typeabonnementbus")
public class TypeAbonnementBus implements Serializable {

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

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @Column(name = "montant_abonnement")
    private Double montantAbonnement;

    @Max(value = 7)
    @Column(name = "duree_abonnement")
    private Integer dureeAbonnement;

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

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Double getMontantAbonnement() {
        return montantAbonnement;
    }

    public void setMontantAbonnement(Double montantAbonnement) {
        this.montantAbonnement = montantAbonnement;
    }

    public Integer getDureeAbonnement() {
        return dureeAbonnement;
    }

    public void setDureeAbonnement(Integer dureeAbonnement) {
        this.dureeAbonnement = dureeAbonnement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypeAbonnementBus typeAbonnementBus = (TypeAbonnementBus) o;
        if(typeAbonnementBus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, typeAbonnementBus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TypeAbonnementBus{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelleFr='" + libelleFr + "'" +
            ", libelleEn='" + libelleEn + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            ", montantAbonnement='" + montantAbonnement + "'" +
            ", dureeAbonnement='" + dureeAbonnement + "'" +
            '}';
    }
}
