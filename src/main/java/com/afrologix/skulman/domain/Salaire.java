package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Salaire.
 */
@Entity
@Table(name = "salaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "salaire")
public class Salaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 200)
    @Column(name = "type_salaire", length = 200)
    private String typeSalaire;

    @Column(name = "montant")
    private Double montant;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeSalaire() {
        return typeSalaire;
    }

    public void setTypeSalaire(String typeSalaire) {
        this.typeSalaire = typeSalaire;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Salaire salaire = (Salaire) o;
        if(salaire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, salaire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Salaire{" +
            "id=" + id +
            ", typeSalaire='" + typeSalaire + "'" +
            ", montant='" + montant + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            '}';
    }
}
