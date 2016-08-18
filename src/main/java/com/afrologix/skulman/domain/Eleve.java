package com.afrologix.skulman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Eleve.
 */
@Entity
@Table(name = "eleve")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "eleve")
public class Eleve implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "matricule", length = 50)
    private String matricule;

    @Size(max = 100)
    @Column(name = "nom", length = 100)
    private String nom;

    @Size(max = 200)
    @Column(name = "prenom", length = 200)
    private String prenom;

    @Column(name = "date_naiss")
    private LocalDate dateNaiss;

    @Size(max = 50)
    @Column(name = "lieu_naiss", length = 50)
    private String lieuNaiss;

    @Size(max = 1)
    @Column(name = "sexe", length = 1)
    private String sexe;

    @Size(max = 50)
    @Column(name = "tel", length = 50)
    private String tel;

    @Size(max = 50)
    @Column(name = "nationalite", length = 50)
    private String nationalite;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

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
    private ChambreEleve chambreEleve;

    @OneToOne
    @JoinColumn(unique = true)
    private Compte compte;

    @OneToOne
    @JoinColumn(unique = true)
    private Religion religion;

    @OneToMany(mappedBy = "eleve")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AbsenceEleve> absenceEleves = new HashSet<>();

    @OneToMany(mappedBy = "eleve")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bourse> bourses = new HashSet<>();

    @OneToMany(mappedBy = "eleve")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Moratoire> moratoires = new HashSet<>();

    @OneToMany(mappedBy = "eleve")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fichier> fichiers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(LocalDate dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getLieuNaiss() {
        return lieuNaiss;
    }

    public void setLieuNaiss(String lieuNaiss) {
        this.lieuNaiss = lieuNaiss;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public ChambreEleve getChambreEleve() {
        return chambreEleve;
    }

    public void setChambreEleve(ChambreEleve chambreEleve) {
        this.chambreEleve = chambreEleve;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Set<AbsenceEleve> getAbsenceEleves() {
        return absenceEleves;
    }

    public void setAbsenceEleves(Set<AbsenceEleve> absenceEleves) {
        this.absenceEleves = absenceEleves;
    }

    public Set<Bourse> getBourses() {
        return bourses;
    }

    public void setBourses(Set<Bourse> bourses) {
        this.bourses = bourses;
    }

    public Set<Moratoire> getMoratoires() {
        return moratoires;
    }

    public void setMoratoires(Set<Moratoire> moratoires) {
        this.moratoires = moratoires;
    }

    public Set<Fichier> getFichiers() {
        return fichiers;
    }

    public void setFichiers(Set<Fichier> fichiers) {
        this.fichiers = fichiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Eleve eleve = (Eleve) o;
        if(eleve.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, eleve.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Eleve{" +
            "id=" + id +
            ", matricule='" + matricule + "'" +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", dateNaiss='" + dateNaiss + "'" +
            ", lieuNaiss='" + lieuNaiss + "'" +
            ", sexe='" + sexe + "'" +
            ", tel='" + tel + "'" +
            ", nationalite='" + nationalite + "'" +
            ", email='" + email + "'" +
            ", createBy='" + createBy + "'" +
            ", updateBy='" + updateBy + "'" +
            ", createAt='" + createAt + "'" +
            ", updateAt='" + updateAt + "'" +
            '}';
    }
}
