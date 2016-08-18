package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Bulletin.
 */
@Entity
@Table(name = "bulletin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bulletin")
public class Bulletin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "moyenne")
    private Double moyenne;

    @Max(value = 1000)
    @Column(name = "rang")
    private Integer rang;

    @Size(max = 20)
    @Column(name = "annee_scolaire", length = 20)
    private String anneeScolaire;

    @OneToOne
    @JoinColumn(unique = true)
    private Eleve eleve;

    @OneToOne
    @JoinColumn(unique = true)
    private Sequence sequence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Double moyenne) {
        this.moyenne = moyenne;
    }

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
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

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bulletin bulletin = (Bulletin) o;
        if(bulletin.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bulletin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bulletin{" +
            "id=" + id +
            ", moyenne='" + moyenne + "'" +
            ", rang='" + rang + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            '}';
    }
}
