package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OptionBulletinNote.
 */
@Entity
@Table(name = "option_bulletin_note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "optionbulletinnote")
public class OptionBulletinNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nom_enseignant")
    private Boolean nomEnseignant;

    @Column(name = "coef")
    private Boolean coef;

    @Column(name = "note_min")
    private Boolean noteMin;

    @Column(name = "note_max")
    private Boolean noteMax;

    @Column(name = "rang_matiere")
    private Boolean rangMatiere;

    @Column(name = "moyenne_matiere")
    private Boolean moyenneMatiere;

    @Column(name = "appreciation")
    private Boolean appreciation;

    @Column(name = "moyenne_generale_classe")
    private Boolean moyenneGeneraleClasse;

    @Column(name = "groupe_matiere")
    private Boolean groupeMatiere;

    @Column(name = "photo")
    private Boolean photo;

    @Column(name = "total_eleve")
    private Boolean totalEleve;

    @Column(name = "afficher_sanction")
    private Boolean afficherSanction;

    @Column(name = "afficher_matricule")
    private Boolean afficherMatricule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isNomEnseignant() {
        return nomEnseignant;
    }

    public void setNomEnseignant(Boolean nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    public Boolean isCoef() {
        return coef;
    }

    public void setCoef(Boolean coef) {
        this.coef = coef;
    }

    public Boolean isNoteMin() {
        return noteMin;
    }

    public void setNoteMin(Boolean noteMin) {
        this.noteMin = noteMin;
    }

    public Boolean isNoteMax() {
        return noteMax;
    }

    public void setNoteMax(Boolean noteMax) {
        this.noteMax = noteMax;
    }

    public Boolean isRangMatiere() {
        return rangMatiere;
    }

    public void setRangMatiere(Boolean rangMatiere) {
        this.rangMatiere = rangMatiere;
    }

    public Boolean isMoyenneMatiere() {
        return moyenneMatiere;
    }

    public void setMoyenneMatiere(Boolean moyenneMatiere) {
        this.moyenneMatiere = moyenneMatiere;
    }

    public Boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(Boolean appreciation) {
        this.appreciation = appreciation;
    }

    public Boolean isMoyenneGeneraleClasse() {
        return moyenneGeneraleClasse;
    }

    public void setMoyenneGeneraleClasse(Boolean moyenneGeneraleClasse) {
        this.moyenneGeneraleClasse = moyenneGeneraleClasse;
    }

    public Boolean isGroupeMatiere() {
        return groupeMatiere;
    }

    public void setGroupeMatiere(Boolean groupeMatiere) {
        this.groupeMatiere = groupeMatiere;
    }

    public Boolean isPhoto() {
        return photo;
    }

    public void setPhoto(Boolean photo) {
        this.photo = photo;
    }

    public Boolean isTotalEleve() {
        return totalEleve;
    }

    public void setTotalEleve(Boolean totalEleve) {
        this.totalEleve = totalEleve;
    }

    public Boolean isAfficherSanction() {
        return afficherSanction;
    }

    public void setAfficherSanction(Boolean afficherSanction) {
        this.afficherSanction = afficherSanction;
    }

    public Boolean isAfficherMatricule() {
        return afficherMatricule;
    }

    public void setAfficherMatricule(Boolean afficherMatricule) {
        this.afficherMatricule = afficherMatricule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OptionBulletinNote optionBulletinNote = (OptionBulletinNote) o;
        if(optionBulletinNote.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, optionBulletinNote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OptionBulletinNote{" +
            "id=" + id +
            ", nomEnseignant='" + nomEnseignant + "'" +
            ", coef='" + coef + "'" +
            ", noteMin='" + noteMin + "'" +
            ", noteMax='" + noteMax + "'" +
            ", rangMatiere='" + rangMatiere + "'" +
            ", moyenneMatiere='" + moyenneMatiere + "'" +
            ", appreciation='" + appreciation + "'" +
            ", moyenneGeneraleClasse='" + moyenneGeneraleClasse + "'" +
            ", groupeMatiere='" + groupeMatiere + "'" +
            ", photo='" + photo + "'" +
            ", totalEleve='" + totalEleve + "'" +
            ", afficherSanction='" + afficherSanction + "'" +
            ", afficherMatricule='" + afficherMatricule + "'" +
            '}';
    }
}
