package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.StatutEleve;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StatutEleve entity.
 */
@SuppressWarnings("unused")
public interface StatutEleveRepository extends JpaRepository<StatutEleve,Long> {

}
