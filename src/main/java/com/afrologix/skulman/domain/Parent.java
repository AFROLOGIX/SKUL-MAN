package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Parent.
 */
@Entity
@Table(name = "parent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "parent")
public class Parent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 100)
    @Column(name = "nom_complet_pere", length = 100)
    private String nomCompletPere;

    @Size(max = 100)
    @Column(name = "nom_complet_mere", length = 100)
    private String nomCompletMere;

    @Size(max = 100)
    @Column(name = "email_pere", length = 100)
    private String emailPere;

    @Size(max = 100)
    @Column(name = "email_mere", length = 100)
    private String emailMere;

    @Size(max = 20)
    @Column(name = "tel_pere", length = 20)
    private String telPere;

    @Size(max = 20)
    @Column(name = "tel_mere", length = 20)
    private String telMere;

    @Size(max = 100)
    @Column(name = "profession_pere", length = 100)
    private String professionPere;

    @Size(max = 100)
    @Column(name = "profession_mere", length = 100)
    private String professionMere;

    @Size(max = 100)
    @Column(name = "nom_complet_tuteur", length = 100)
    private String nomCompletTuteur;

    @Size(max = 100)
    @Column(name = "email_tuteur", length = 100)
    private String emailTuteur;

    @Size(max = 20)
    @Column(name = "tel_tuteur", length = 20)
    private String telTuteur;

    @Size(max = 100)
    @Column(name = "profession_tuteur", length = 100)
    private String professionTuteur;

    @OneToOne
    @JoinColumn(unique = true)
    private Eleve eleve;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCompletPere() {
        return nomCompletPere;
    }

    public void setNomCompletPere(String nomCompletPere) {
        this.nomCompletPere = nomCompletPere;
    }

    public String getNomCompletMere() {
        return nomCompletMere;
    }

    public void setNomCompletMere(String nomCompletMere) {
        this.nomCompletMere = nomCompletMere;
    }

    public String getEmailPere() {
        return emailPere;
    }

    public void setEmailPere(String emailPere) {
        this.emailPere = emailPere;
    }

    public String getEmailMere() {
        return emailMere;
    }

    public void setEmailMere(String emailMere) {
        this.emailMere = emailMere;
    }

    public String getTelPere() {
        return telPere;
    }

    public void setTelPere(String telPere) {
        this.telPere = telPere;
    }

    public String getTelMere() {
        return telMere;
    }

    public void setTelMere(String telMere) {
        this.telMere = telMere;
    }

    public String getProfessionPere() {
        return professionPere;
    }

    public void setProfessionPere(String professionPere) {
        this.professionPere = professionPere;
    }

    public String getProfessionMere() {
        return professionMere;
    }

    public void setProfessionMere(String professionMere) {
        this.professionMere = professionMere;
    }

    public String getNomCompletTuteur() {
        return nomCompletTuteur;
    }

    public void setNomCompletTuteur(String nomCompletTuteur) {
        this.nomCompletTuteur = nomCompletTuteur;
    }

    public String getEmailTuteur() {
        return emailTuteur;
    }

    public void setEmailTuteur(String emailTuteur) {
        this.emailTuteur = emailTuteur;
    }

    public String getTelTuteur() {
        return telTuteur;
    }

    public void setTelTuteur(String telTuteur) {
        this.telTuteur = telTuteur;
    }

    public String getProfessionTuteur() {
        return professionTuteur;
    }

    public void setProfessionTuteur(String professionTuteur) {
        this.professionTuteur = professionTuteur;
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
        Parent parent = (Parent) o;
        if(parent.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, parent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Parent{" +
            "id=" + id +
            ", nomCompletPere='" + nomCompletPere + "'" +
            ", nomCompletMere='" + nomCompletMere + "'" +
            ", emailPere='" + emailPere + "'" +
            ", emailMere='" + emailMere + "'" +
            ", telPere='" + telPere + "'" +
            ", telMere='" + telMere + "'" +
            ", professionPere='" + professionPere + "'" +
            ", professionMere='" + professionMere + "'" +
            ", nomCompletTuteur='" + nomCompletTuteur + "'" +
            ", emailTuteur='" + emailTuteur + "'" +
            ", telTuteur='" + telTuteur + "'" +
            ", professionTuteur='" + professionTuteur + "'" +
            '}';
    }
}
