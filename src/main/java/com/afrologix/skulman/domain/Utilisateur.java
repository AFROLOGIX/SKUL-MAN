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
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "utilisateur")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "code", length = 50)
    private String code;

    @Size(max = 100)
    @Column(name = "pwd", length = 100)
    private String pwd;

    @Size(max = 100)
    @Column(name = "nom", length = 100)
    private String nom;

    @Size(max = 100)
    @Column(name = "prenom", length = 100)
    private String prenom;

    @Size(max = 20)
    @Column(name = "tel", length = 20)
    private String tel;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "date_connexion")
    private ZonedDateTime dateConnexion;

    @OneToOne
    @JoinColumn(unique = true)
    private GroupeUtilisateur groupeUtilisateur;

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public ZonedDateTime getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(ZonedDateTime dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public GroupeUtilisateur getGroupeUtilisateur() {
        return groupeUtilisateur;
    }

    public void setGroupeUtilisateur(GroupeUtilisateur groupeUtilisateur) {
        this.groupeUtilisateur = groupeUtilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Utilisateur utilisateur = (Utilisateur) o;
        if(utilisateur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, utilisateur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", pwd='" + pwd + "'" +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", tel='" + tel + "'" +
            ", email='" + email + "'" +
            ", dateConnexion='" + dateConnexion + "'" +
            '}';
    }
}
