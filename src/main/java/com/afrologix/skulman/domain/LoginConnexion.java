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
 * A LoginConnexion.
 */
@Entity
@Table(name = "login_connexion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "loginconnexion")
public class LoginConnexion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "role", length = 50)
    private String role;

    @Column(name = "login_time")
    private ZonedDateTime loginTime;

    @Column(name = "status")
    private Boolean status;

    @Size(max = 50)
    @Column(name = "address_ip", length = 50)
    private String addressIp;

    @Max(value = 10000)
    @Column(name = "nb_echecs")
    private Integer nbEchecs;

    @Column(name = "date_echec")
    private ZonedDateTime dateEchec;

    @OneToOne
    @JoinColumn(unique = true)
    private Utilisateur utilisateur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ZonedDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(ZonedDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAddressIp() {
        return addressIp;
    }

    public void setAddressIp(String addressIp) {
        this.addressIp = addressIp;
    }

    public Integer getNbEchecs() {
        return nbEchecs;
    }

    public void setNbEchecs(Integer nbEchecs) {
        this.nbEchecs = nbEchecs;
    }

    public ZonedDateTime getDateEchec() {
        return dateEchec;
    }

    public void setDateEchec(ZonedDateTime dateEchec) {
        this.dateEchec = dateEchec;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginConnexion loginConnexion = (LoginConnexion) o;
        if(loginConnexion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, loginConnexion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LoginConnexion{" +
            "id=" + id +
            ", role='" + role + "'" +
            ", loginTime='" + loginTime + "'" +
            ", status='" + status + "'" +
            ", addressIp='" + addressIp + "'" +
            ", nbEchecs='" + nbEchecs + "'" +
            ", dateEchec='" + dateEchec + "'" +
            '}';
    }
}
