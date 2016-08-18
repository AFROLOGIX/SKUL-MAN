package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StatutEleve.
 */
@Entity
@Table(name = "statut_eleve")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "statuteleve")
public class StatutEleve implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "redouble")
    private Boolean redouble;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @OneToOne
    @JoinColumn(unique = true)
    private Eleve eleve;

    @OneToOne
    @JoinColumn(unique = true)
    private Statut statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isRedouble() {
        return redouble;
    }

    public void setRedouble(Boolean redouble) {
        this.redouble = redouble;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatutEleve statutEleve = (StatutEleve) o;
        if(statutEleve.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, statutEleve.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StatutEleve{" +
            "id=" + id +
            ", redouble='" + redouble + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            '}';
    }
}
