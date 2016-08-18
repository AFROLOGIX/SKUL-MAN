package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MoyenneTableauHonneur.
 */
@Entity
@Table(name = "moyenne_tableau_honneur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "moyennetableauhonneur")
public class MoyenneTableauHonneur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "condition_tableau_honneur")
    private Double conditionTableauHonneur;

    @Column(name = "condition_encouragement")
    private Double conditionEncouragement;

    @Column(name = "condition_felicitation")
    private Double conditionFelicitation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getConditionTableauHonneur() {
        return conditionTableauHonneur;
    }

    public void setConditionTableauHonneur(Double conditionTableauHonneur) {
        this.conditionTableauHonneur = conditionTableauHonneur;
    }

    public Double getConditionEncouragement() {
        return conditionEncouragement;
    }

    public void setConditionEncouragement(Double conditionEncouragement) {
        this.conditionEncouragement = conditionEncouragement;
    }

    public Double getConditionFelicitation() {
        return conditionFelicitation;
    }

    public void setConditionFelicitation(Double conditionFelicitation) {
        this.conditionFelicitation = conditionFelicitation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoyenneTableauHonneur moyenneTableauHonneur = (MoyenneTableauHonneur) o;
        if(moyenneTableauHonneur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, moyenneTableauHonneur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MoyenneTableauHonneur{" +
            "id=" + id +
            ", conditionTableauHonneur='" + conditionTableauHonneur + "'" +
            ", conditionEncouragement='" + conditionEncouragement + "'" +
            ", conditionFelicitation='" + conditionFelicitation + "'" +
            '}';
    }
}
