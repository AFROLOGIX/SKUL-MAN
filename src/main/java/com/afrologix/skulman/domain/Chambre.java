package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Chambre.
 */
@Entity
@Table(name = "chambre")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "chambre")
public class Chambre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 25)
    @Column(name = "code", length = 25)
    private String code;

    @Size(max = 100)
    @Column(name = "libelle", length = 100)
    private String libelle;

    @Max(value = 10)
    @Column(name = "nombre_max_personne")
    private Integer nombreMaxPersonne;

    @OneToOne
    @JoinColumn(unique = true)
    private Batiment batiment;

    @OneToOne
    @JoinColumn(unique = true)
    private TypeChambre typeChambre;

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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getNombreMaxPersonne() {
        return nombreMaxPersonne;
    }

    public void setNombreMaxPersonne(Integer nombreMaxPersonne) {
        this.nombreMaxPersonne = nombreMaxPersonne;
    }

    public Batiment getBatiment() {
        return batiment;
    }

    public void setBatiment(Batiment batiment) {
        this.batiment = batiment;
    }

    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chambre chambre = (Chambre) o;
        if(chambre.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, chambre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Chambre{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", libelle='" + libelle + "'" +
            ", nombreMaxPersonne='" + nombreMaxPersonne + "'" +
            '}';
    }
}
