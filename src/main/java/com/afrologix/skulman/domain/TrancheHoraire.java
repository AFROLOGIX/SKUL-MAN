package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TrancheHoraire.
 */
@Entity
@Table(name = "tranche_horaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tranchehoraire")
public class TrancheHoraire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private TypeTrancheHoraire typeTrancheHoraire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeTrancheHoraire getTypeTrancheHoraire() {
        return typeTrancheHoraire;
    }

    public void setTypeTrancheHoraire(TypeTrancheHoraire typeTrancheHoraire) {
        this.typeTrancheHoraire = typeTrancheHoraire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrancheHoraire trancheHoraire = (TrancheHoraire) o;
        if(trancheHoraire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, trancheHoraire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TrancheHoraire{" +
            "id=" + id +
            '}';
    }
}
