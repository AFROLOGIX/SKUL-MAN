package com.afrologix.skulman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AgentAdministratif.
 */
@Entity
@Table(name = "agent_administratif")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "agentadministratif")
public class AgentAdministratif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "role", length = 50)
    private String role;

    @OneToOne
    @JoinColumn(unique = true)
    private Salaire salaire;

    @OneToMany(mappedBy = "agentAdministratif")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Fichier> fichiers = new HashSet<>();

    @OneToMany(mappedBy = "agentAdministratif")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Deliberation> deliberations = new HashSet<>();

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

    public Salaire getSalaire() {
        return salaire;
    }

    public void setSalaire(Salaire salaire) {
        this.salaire = salaire;
    }

    public Set<Fichier> getFichiers() {
        return fichiers;
    }

    public void setFichiers(Set<Fichier> fichiers) {
        this.fichiers = fichiers;
    }

    public Set<Deliberation> getDeliberations() {
        return deliberations;
    }

    public void setDeliberations(Set<Deliberation> deliberations) {
        this.deliberations = deliberations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentAdministratif agentAdministratif = (AgentAdministratif) o;
        if(agentAdministratif.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, agentAdministratif.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AgentAdministratif{" +
            "id=" + id +
            ", role='" + role + "'" +
            '}';
    }
}
