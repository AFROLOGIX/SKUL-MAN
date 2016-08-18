package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.ChambreEleve;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ChambreEleve entity.
 */
@SuppressWarnings("unused")
public interface ChambreEleveRepository extends JpaRepository<ChambreEleve,Long> {

}
