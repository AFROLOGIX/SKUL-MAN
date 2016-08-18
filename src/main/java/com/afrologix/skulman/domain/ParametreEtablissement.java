package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ParametreEtablissement.
 */
@Entity
@Table(name = "parametre_etablissement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "parametreetablissement")
public class ParametreEtablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @Column(name = "activer_enregistrement_bulletin_note_bd")
    private Boolean activerEnregistrementBulletinNoteBd;

    @Column(name = "heure_deb_cours")
    private ZonedDateTime heureDebCours;

    @Column(name = "heure_fin_cours")
    private ZonedDateTime heureFinCours;

    @Size(max = 100)
    @Column(name = "regime", length = 100)
    private String regime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Boolean isActiverEnregistrementBulletinNoteBd() {
        return activerEnregistrementBulletinNoteBd;
    }

    public void setActiverEnregistrementBulletinNoteBd(Boolean activerEnregistrementBulletinNoteBd) {
        this.activerEnregistrementBulletinNoteBd = activerEnregistrementBulletinNoteBd;
    }

    public ZonedDateTime getHeureDebCours() {
        return heureDebCours;
    }

    public void setHeureDebCours(ZonedDateTime heureDebCours) {
        this.heureDebCours = heureDebCours;
    }

    public ZonedDateTime getHeureFinCours() {
        return heureFinCours;
    }

    public void setHeureFinCours(ZonedDateTime heureFinCours) {
        this.heureFinCours = heureFinCours;
    }

    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParametreEtablissement parametreEtablissement = (ParametreEtablissement) o;
        if(parametreEtablissement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, parametreEtablissement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ParametreEtablissement{" +
            "id=" + id +
            ", anneeScolaire='" + anneeScolaire + "'" +
            ", activerEnregistrementBulletinNoteBd='" + activerEnregistrementBulletinNoteBd + "'" +
            ", heureDebCours='" + heureDebCours + "'" +
            ", heureFinCours='" + heureFinCours + "'" +
            ", regime='" + regime + "'" +
            '}';
    }
}
