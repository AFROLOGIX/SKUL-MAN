package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Appreciation.
 */
@Entity
@Table(name = "appreciation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "appreciation")
public class Appreciation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "code", length = 50)
    private String code;

    @Size(max = 100)
    @Column(name = "libelle_fr", length = 100)
    private String libelleFr;

    @Size(max = 100)
    @Column(name = "libelle_en", length = 100)
    private String libelleEn;

    @Column(name = "min_note")
    private Double minNote;

    @Column(name = "max_note")
    private Double maxNote;

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

    public Double getMinNote() {
        return minNote;
    }

    public void setMinNote(Double minNote) {
        this.minNote = minNote;
    }

    public Double getMaxNote() {
        return maxNote;
    }

    public void setMaxNote(Double maxNote) {
        this.maxNote = maxNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Appreciation appreciation = (Appreciation) o;
        if(appreciation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, appreciation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Appreciation{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelleFr='" + libelleFr + "'" +
            ", libelleEn='" + libelleEn + "'" +
            ", minNote='" + minNote + "'" +
            ", maxNote='" + maxNote + "'" +
            '}';
    }
}
