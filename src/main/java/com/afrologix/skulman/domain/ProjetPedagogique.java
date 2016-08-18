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
 * A ProjetPedagogique.
 */
@Entity
@Table(name = "projet_pedagogique")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "projetpedagogique")
public class ProjetPedagogique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 100)
    @Column(name = "element_prog", length = 100)
    private String elementProg;

    @Column(name = "volume_horaire")
    private Double volumeHoraire;

    @Column(name = "date_deb")
    private LocalDate dateDeb;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "status")
    private Boolean status;

    @OneToOne
    @JoinColumn(unique = true)
    private Classe classe;

    @ManyToOne
    private Enseignant enseigant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElementProg() {
        return elementProg;
    }

    public void setElementProg(String elementProg) {
        this.elementProg = elementProg;
    }

    public Double getVolumeHoraire() {
        return volumeHoraire;
    }

    public void setVolumeHoraire(Double volumeHoraire) {
        this.volumeHoraire = volumeHoraire;
    }

    public LocalDate getDateDeb() {
        return dateDeb;
    }

    public void setDateDeb(LocalDate dateDeb) {
        this.dateDeb = dateDeb;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Enseignant getEnseigant() {
        return enseigant;
    }

    public void setEnseigant(Enseignant enseignant) {
        this.enseigant = enseignant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjetPedagogique projetPedagogique = (ProjetPedagogique) o;
        if(projetPedagogique.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projetPedagogique.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjetPedagogique{" +
            "id=" + id +
            ", elementProg='" + elementProg + "'" +
            ", volumeHoraire='" + volumeHoraire + "'" +
            ", dateDeb='" + dateDeb + "'" +
            ", dateFin='" + dateFin + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
