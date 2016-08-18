package com.afrologix.skulman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RegimePension.
 */
@Entity
@Table(name = "regime_pension")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "regimepension")
public class RegimePension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "code", length = 50)
    private String code;

    @Size(max = 200)
    @Column(name = "libelle_fr", length = 200)
    private String libelleFr;

    @Size(max = 200)
    @Column(name = "libelle_en", length = 200)
    private String libelleEn;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @Max(value = 10)
    @Column(name = "nb_total_tranches")
    private Integer nbTotalTranches;

    @OneToMany(mappedBy = "regimePension")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tranche> tranches = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelleFr() {
        return libelleFr;
    }

    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    public String getLibelleEn() {
        return libelleEn;
    }

    public void setLibelleEn(String libelleEn) {
        this.libelleEn = libelleEn;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Integer getNbTotalTranches() {
        return nbTotalTranches;
    }

    public void setNbTotalTranches(Integer nbTotalTranches) {
        this.nbTotalTranches = nbTotalTranches;
    }

    public Set<Tranche> getTranches() {
        return tranches;
    }

    public void setTranches(Set<Tranche> tranches) {
        this.tranches = tranches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegimePension regimePension = (RegimePension) o;
        if(regimePension.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, regimePension.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RegimePension{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelleFr='" + libelleFr + "'" +
            ", libelleEn='" + libelleEn + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            ", nbTotalTranches='" + nbTotalTranches + "'" +
            '}';
    }
}
