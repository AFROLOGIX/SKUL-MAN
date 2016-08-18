package com.afrologix.skulman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Personnel.
 */
@Entity
@Table(name = "personnel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "personnel")
public class Personnel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 100)
    @Column(name = "nom", length = 100)
    private String nom;

    @Size(max = 200)
    @Column(name = "prenom", length = 200)
    private String prenom;

    @Size(max = 20)
    @Column(name = "tel", length = 20)
    private String tel;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "date_admission")
    private LocalDate dateAdmission;

    @Column(name = "is_active")
    private Boolean isActive;

    @Size(max = 100)
    @Column(name = "adresse", length = 100)
    private String adresse;

    @OneToOne
    @JoinColumn(unique = true)
    private Utilisateur utilisateur;

    @OneToOne
    @JoinColumn(unique = true)
    private TypePersonnel typePersonnel;

    @OneToMany(mappedBy = "personnel")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AbsencePersonnel> absencePersonnels = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateAdmission() {
        return dateAdmission;
    }

    public void setDateAdmission(LocalDate dateAdmission) {
        this.dateAdmission = dateAdmission;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public TypePersonnel getTypePersonnel() {
        return typePersonnel;
    }

    public void setTypePersonnel(TypePersonnel typePersonnel) {
        this.typePersonnel = typePersonnel;
    }

    public Set<AbsencePersonnel> getAbsencePersonnels() {
        return absencePersonnels;
    }

    public void setAbsencePersonnels(Set<AbsencePersonnel> absencePersonnels) {
        this.absencePersonnels = absencePersonnels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Personnel personnel = (Personnel) o;
        if(personnel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, personnel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Personnel{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", tel='" + tel + "'" +
            ", email='" + email + "'" +
            ", dateAdmission='" + dateAdmission + "'" +
            ", isActive='" + isActive + "'" +
            ", adresse='" + adresse + "'" +
            '}';
    }
}
