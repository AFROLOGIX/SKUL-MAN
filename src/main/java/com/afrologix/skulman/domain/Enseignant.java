package com.afrologix.skulman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Enseignant.
 */
@Entity
@Table(name = "enseignant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "enseignant")
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "matricule", length = 50)
    private String matricule;

    @Size(max = 50)
    @Column(name = "specialite", length = 50)
    private String specialite;

    @OneToOne
    @JoinColumn(unique = true)
    private Personnel personnel;

    @OneToMany(mappedBy = "enseignant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AbsenceEnseignant> absenceEnseignants = new HashSet<>();

    @OneToMany(mappedBy = "enseignant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vacation> vacations = new HashSet<>();

    @OneToMany(mappedBy = "enseignant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fichier> fichiers = new HashSet<>();

    @OneToMany(mappedBy = "enseigant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProjetPedagogique> projetPedagogiques = new HashSet<>();

    @OneToMany(mappedBy = "enseignant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Deliberation> deliberations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public Set<AbsenceEnseignant> getAbsenceEnseignants() {
        return absenceEnseignants;
    }

    public void setAbsenceEnseignants(Set<AbsenceEnseignant> absenceEnseignants) {
        this.absenceEnseignants = absenceEnseignants;
    }

    public Set<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(Set<Vacation> vacations) {
        this.vacations = vacations;
    }

    public Set<Fichier> getFichiers() {
        return fichiers;
    }

    public void setFichiers(Set<Fichier> fichiers) {
        this.fichiers = fichiers;
    }

    public Set<ProjetPedagogique> getProjetPedagogiques() {
        return projetPedagogiques;
    }

    public void setProjetPedagogiques(Set<ProjetPedagogique> projetPedagogiques) {
        this.projetPedagogiques = projetPedagogiques;
    }

    public Set<Deliberation> getDeliberations() {
        return deliberations;
    }

    public void setDeliberations(Set<Deliberation> deliberations) {
        this.deliberations = deliberations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enseignant enseignant = (Enseignant) o;
        if(enseignant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, enseignant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Enseignant{" +
            "id=" + id +
            ", matricule='" + matricule + "'" +
            ", specialite='" + specialite + "'" +
            '}';
    }
}
