package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TransactionDiverses.
 */
@Entity
@Table(name = "transaction_diverses")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "transactiondiverses")
public class TransactionDiverses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 100)
    @Column(name = "type_usager", length = 100)
    private String typeUsager;

    @Column(name = "is_credit")
    private Boolean isCredit;

    @Column(name = "usager_id")
    private Integer usagerId;

    @OneToOne
    @JoinColumn(unique = true)
    private AgentAdministratif agentAdministratif;

    @OneToOne
    @JoinColumn(unique = true)
    private Operation operation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeUsager() {
        return typeUsager;
    }

    public void setTypeUsager(String typeUsager) {
        this.typeUsager = typeUsager;
    }

    public Boolean isIsCredit() {
        return isCredit;
    }

    public void setIsCredit(Boolean isCredit) {
        this.isCredit = isCredit;
    }

    public Integer getUsagerId() {
        return usagerId;
    }

    public void setUsagerId(Integer usagerId) {
        this.usagerId = usagerId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionDiverses transactionDiverses = (TransactionDiverses) o;
        if(transactionDiverses.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transactionDiverses.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TransactionDiverses{" +
            "id=" + id +
            ", typeUsager='" + typeUsager + "'" +
            ", isCredit='" + isCredit + "'" +
            ", usagerId='" + usagerId + "'" +
            '}';
    }
}
