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
 * A Vacation.
 */
@Entity
@Table(name = "vacation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vacation")
public class Vacation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 100)
    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "date")
    private LocalDate date;

    @Size(max = 50)
    @Column(name = "periode", length = 50)
    private String periode;

    @Size(max = 50)
    @Column(name = "classe", length = 50)
    private String classe;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @ManyToOne
    private Enseignant enseignant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacation vacation = (Vacation) o;
        if(vacation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, vacation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Vacation{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", date='" + date + "'" +
            ", periode='" + periode + "'" +
            ", classe='" + classe + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            '}';
    }
}
