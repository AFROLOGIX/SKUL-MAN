package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PaiementPersonnel.
 */
@Entity
@Table(name = "paiement_personnel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "paiementpersonnel")
public class PaiementPersonnel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "montant", precision=10, scale=2)
    private BigDecimal montant;

    @Column(name = "dette", precision=10, scale=2)
    private BigDecimal dette;

    @Size(max = 50)
    @Column(name = "annee_scolaire", length = 50)
    private String anneeScolaire;

    @Size(max = 50)
    @Column(name = "periode", length = 50)
    private String periode;

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
    private AgentAdministratif agentAdministratif;

    @OneToOne
    @JoinColumn(unique = true)
    private Operation operation;

    @OneToOne
    @JoinColumn(unique = true)
    private Personnel personnel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getDette() {
        return dette;
    }

    public void setDette(BigDecimal dette) {
        this.dette = dette;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
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

    public AgentAdministratif getAgentAdministratif() {
        return agentAdministratif;
    }

    public void setAgentAdministratif(AgentAdministratif agentAdministratif) {
        this.agentAdministratif = agentAdministratif;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaiementPersonnel paiementPersonnel = (PaiementPersonnel) o;
        if(paiementPersonnel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, paiementPersonnel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PaiementPersonnel{" +
            "id=" + id +
            ", montant='" + montant + "'" +
            ", dette='" + dette + "'" +
            ", anneeScolaire='" + anneeScolaire + "'" +
            ", periode='" + periode + "'" +
            ", createBy='" + createBy + "'" +
            ", updateBy='" + updateBy + "'" +
            ", createAt='" + createAt + "'" +
            ", updateAt='" + updateAt + "'" +
            '}';
    }
}
