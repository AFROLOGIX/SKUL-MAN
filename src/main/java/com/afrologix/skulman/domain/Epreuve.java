package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Epreuve.
 */
@Entity
@Table(name = "epreuve")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "epreuve")
public class Epreuve implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @OneToOne
    @JoinColumn(unique = true)
    private TypeEpreuve typeEpreuve;

    @ManyToOne
    private Cours cours;

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

    public TypeEpreuve getTypeEpreuve() {
        return typeEpreuve;
    }

    public void setTypeEpreuve(TypeEpreuve typeEpreuve) {
        this.typeEpreuve = typeEpreuve;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Epreuve epreuve = (Epreuve) o;
        if(epreuve.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, epreuve.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Epreuve{" +
            "id=" + id +
            ", anneeScolaire='" + anneeScolaire + "'" +
            '}';
    }
}
