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
 * A Deliberation.
 */
@Entity
@Table(name = "deliberation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "deliberation")
public class Deliberation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 300)
    @Column(name = "motif", length = 300)
    private String motif;

    @Size(max = 300)
    @Column(name = "decision", length = 300)
    private String decision;

    @Column(name = "is_active")
    private Boolean isActive;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

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
    private Eleve eleve;

    @ManyToOne
    private AgentAdministratif agentAdministratif;

    @ManyToOne
    private Enseignant enseignant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
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

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public AgentAdministratif getAgentAdministratif() {
        return agentAdministratif;
    }

    public void setAgentAdministratif(AgentAdministratif agentAdministratif) {
        this.agentAdministratif = agentAdministratif;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Deliberation deliberation = (Deliberation) o;
        if(deliberation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, deliberation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Deliberation{" +
            "id=" + id +
            ", motif='" + motif + "'" +
            ", decision='" + decision + "'" +
            ", isActive='" + isActive + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            ", createBy='" + createBy + "'" +
            ", updateBy='" + updateBy + "'" +
            ", createAt='" + createAt + "'" +
            ", updateAt='" + updateAt + "'" +
            '}';
    }
}
