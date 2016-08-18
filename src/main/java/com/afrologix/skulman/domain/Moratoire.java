package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Moratoire.
 */
@Entity
@Table(name = "moratoire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "moratoire")
public class Moratoire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 100)
    @Column(name = "motif", length = 100)
    private String motif;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @Column(name = "nouvelle_date")
    private LocalDate nouvelleDate;

    @Size(max = 50)
    @Column(name = "type", length = 50)
    private String type;

    @Size(max = 50)
    @Column(name = "create_by", length = 50)
    private String createBy;

    @Size(max = 50)
    @Column(name = "update_by", length = 50)
    private String updateBy;

    @Column(name = "create_at")
    private ZonedDateTime createAt;

    @Column(name = "update_at")
    private ZonedDateTime updateAt;

    @OneToOne
    @JoinColumn(unique = true)
    private TypeMoratoire typeMoratoire;

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

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public LocalDate getNouvelleDate() {
        return nouvelleDate;
    }

    public void setNouvelleDate(LocalDate nouvelleDate) {
        this.nouvelleDate = nouvelleDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public TypeMoratoire getTypeMoratoire() {
        return typeMoratoire;
    }

    public void setTypeMoratoire(TypeMoratoire typeMoratoire) {
        this.typeMoratoire = typeMoratoire;
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
        Moratoire moratoire = (Moratoire) o;
        if(moratoire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, moratoire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Moratoire{" +
            "id=" + id +
            ", motif='" + motif + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            ", nouvelleDate='" + nouvelleDate + "'" +
            ", type='" + type + "'" +
            ", createBy='" + createBy + "'" +
            ", updateBy='" + updateBy + "'" +
            ", createAt='" + createAt + "'" +
            ", updateAt='" + updateAt + "'" +
            '}';
    }
}
