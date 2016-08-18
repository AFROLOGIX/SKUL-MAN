package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TrancheHoraire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TrancheHoraire entity.
 */
@SuppressWarnings("unused")
public interface TrancheHoraireRepository extends JpaRepository<TrancheHoraire,Long> {

}
