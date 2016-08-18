package com.afrologix.skulman.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Bus.
 */
@Entity
@Table(name = "bus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "bus")
public class Bus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    @Column(name = "immatriculation", length = 50)
    private String immatriculation;

    @Size(max = 200)
    @Column(name = "libelle_fr", length = 200)
    private String libelleFr;

    @Size(max = 200)
    @Column(name = "libelle_en", length = 200)
    private String libelleEn;

    @Max(value = 120)
    @Column(name = "capacite")
    private Integer capacite;

    @Size(max = 50)
    @Column(name = "marque", length = 50)
    private String marque;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne
    @JoinColumn(unique = true)
    private TarifBus tarifBus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getLibelleFr() {
        return libelleFr;
    }

    public void setLibelleFr(String libelleFr) {
        this.libelleFr = libelleFr;
    }

    public String getLibelleEn() {
        return libelleEn;
    }

    public void setLibelleEn(String libelleEn) {
        this.libelleEn = libelleEn;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public LocalDate getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public TarifBus getTarifBus() {
        return tarifBus;
    }

    public void setTarifBus(TarifBus tarifBus) {
        this.tarifBus = tarifBus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bus bus = (Bus) o;
        if(bus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bus{" +
            "id=" + id +
            ", immatriculation='" + immatriculation + "'" +
            ", libelleFr='" + libelleFr + "'" +
            ", libelleEn='" + libelleEn + "'" +
            ", capacite='" + capacite + "'" +
            ", marque='" + marque + "'" +
            ", dateAcquisition='" + dateAcquisition + "'" +
            ", isActive='" + isActive + "'" +
            '}';
    }
}
