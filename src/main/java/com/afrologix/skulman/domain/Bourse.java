package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Bourse.
 */
@Entity
@Table(name = "bourse")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bourse")
public class Bourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 200)
    @Column(name = "motif", length = 200)
    private String motif;

    @NotNull
    @Column(name = "montant", precision=10, scale=2, nullable = false)
    private BigDecimal montant;

    @Size(max = 200)
    @Column(name = "annee_scolaire", length = 200)
    private String anneeScolaire;

    @ManyToOne
    private Eleve eleve;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bourse bourse = (Bourse) o;
        if(bourse.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bourse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bourse{" +
            "id=" + id +
            ", motif='" + motif + "'" +
            ", montant='" + montant + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            '}';
    }
}
