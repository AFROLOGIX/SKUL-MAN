package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Droit.
 */
@Entity
@Table(name = "droit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "droit")
public class Droit implements Serializable {

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

    @OneToOne
    @JoinColumn(unique = true)
    private Fonctionnalite fonctionnalite;

    @OneToOne
    @JoinColumn(unique = true)
    private GroupeUtilisateur groupeUtilisateur;

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

    public Fonctionnalite getFonctionnalite() {
        return fonctionnalite;
    }

    public void setFonctionnalite(Fonctionnalite fonctionnalite) {
        this.fonctionnalite = fonctionnalite;
    }

    public GroupeUtilisateur getGroupeUtilisateur() {
        return groupeUtilisateur;
    }

    public void setGroupeUtilisateur(GroupeUtilisateur groupeUtilisateur) {
        this.groupeUtilisateur = groupeUtilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Droit droit = (Droit) o;
        if(droit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, droit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Droit{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelleFr='" + libelleFr + "'" +
            ", libelleEn='" + libelleEn + "'" +
            '}';
    }
}
