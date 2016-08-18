package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ClasseEleve.
 */
@Entity
@Table(name = "classe_eleve")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "classeeleve")
public class ClasseEleve implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @Column(name = "is_active")
    private Boolean isActive;

    @Size(max = 250)
    @Column(name = "observation", length = 250)
    private String observation;

    @OneToOne
    @JoinColumn(unique = true)
    private Classe classe;

    @OneToOne
    @JoinColumn(unique = true)
    private Eleve eleve;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClasseEleve classeEleve = (ClasseEleve) o;
        if(classeEleve.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, classeEleve.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClasseEleve{" +
            "id=" + id +
            ", anneeScolaire='" + anneeScolaire + "'" +
            ", isActive='" + isActive + "'" +
            ", observation='" + observation + "'" +
            '}';
    }
}
