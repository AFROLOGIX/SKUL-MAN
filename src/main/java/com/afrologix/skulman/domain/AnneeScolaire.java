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
 * A AnneeScolaire.
 */
@Entity
@Table(name = "annee_scolaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "anneescolaire")
public class AnneeScolaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "annee", length = 50)
    private String annee;

    @Size(max = 25)
    @Column(name = "code_annee", length = 25)
    private String codeAnnee;

    @Column(name = "date_deb")
    private LocalDate dateDeb;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "objectifs")
    private String objectifs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getCodeAnnee() {
        return codeAnnee;
    }

    public void setCodeAnnee(String codeAnnee) {
        this.codeAnnee = codeAnnee;
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

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getObjectifs() {
        return objectifs;
    }

    public void setObjectifs(String objectifs) {
        this.objectifs = objectifs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnneeScolaire anneeScolaire = (AnneeScolaire) o;
        if(anneeScolaire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, anneeScolaire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AnneeScolaire{" +
            "id=" + id +
            ", annee='" + annee + "'" +
            ", codeAnnee='" + codeAnnee + "'" +
            ", dateDeb='" + dateDeb + "'" +
            ", dateFin='" + dateFin + "'" +
            ", isActive='" + isActive + "'" +
            ", objectifs='" + objectifs + "'" +
            '}';
    }
}
