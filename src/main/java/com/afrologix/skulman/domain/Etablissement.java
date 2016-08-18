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
 * A Etablissement.
 */
@Entity
@Table(name = "etablissement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "etablissement")
public class Etablissement implements Serializable {

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

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Size(max = 75)
    @Column(name = "titre_responsable", length = 75)
    private String titreResponsable;

    @Size(max = 75)
    @Column(name = "ville", length = 75)
    private String ville;

    @Size(max = 100)
    @Column(name = "nom_reponsable", length = 100)
    private String nomReponsable;

    @Size(max = 100)
    @Column(name = "site_web", length = 100)
    private String siteWeb;

    @Size(max = 200)
    @Column(name = "chemin_logo", length = 200)
    private String cheminLogo;

    @Max(value = 10)
    @Column(name = "nb_trimestre")
    private Integer nbTrimestre;

    @Max(value = 10)
    @Column(name = "nb_sequence")
    private Integer nbSequence;

    @Size(max = 100)
    @Column(name = "bp", length = 100)
    private String bp;

    @Size(max = 200)
    @Column(name = "localisation", length = 200)
    private String localisation;

    @Size(max = 50)
    @Column(name = "tel", length = 50)
    private String tel;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 100)
    @Column(name = "devise", length = 100)
    private String devise;

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

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getTitreResponsable() {
        return titreResponsable;
    }

    public void setTitreResponsable(String titreResponsable) {
        this.titreResponsable = titreResponsable;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getNomReponsable() {
        return nomReponsable;
    }

    public void setNomReponsable(String nomReponsable) {
        this.nomReponsable = nomReponsable;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getCheminLogo() {
        return cheminLogo;
    }

    public void setCheminLogo(String cheminLogo) {
        this.cheminLogo = cheminLogo;
    }

    public Integer getNbTrimestre() {
        return nbTrimestre;
    }

    public void setNbTrimestre(Integer nbTrimestre) {
        this.nbTrimestre = nbTrimestre;
    }

    public Integer getNbSequence() {
        return nbSequence;
    }

    public void setNbSequence(Integer nbSequence) {
        this.nbSequence = nbSequence;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Etablissement etablissement = (Etablissement) o;
        if(etablissement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, etablissement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelleFr='" + libelleFr + "'" +
            ", libelleEn='" + libelleEn + "'" +
            ", dateCreation='" + dateCreation + "'" +
            ", titreResponsable='" + titreResponsable + "'" +
            ", ville='" + ville + "'" +
            ", nomReponsable='" + nomReponsable + "'" +
            ", siteWeb='" + siteWeb + "'" +
            ", cheminLogo='" + cheminLogo + "'" +
            ", nbTrimestre='" + nbTrimestre + "'" +
            ", nbSequence='" + nbSequence + "'" +
            ", bp='" + bp + "'" +
            ", localisation='" + localisation + "'" +
            ", tel='" + tel + "'" +
            ", email='" + email + "'" +
            ", devise='" + devise + "'" +
            '}';
    }
}
