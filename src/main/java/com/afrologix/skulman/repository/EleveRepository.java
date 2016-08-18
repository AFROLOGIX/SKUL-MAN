package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Eleve;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Eleve entity.
 */
@SuppressWarnings("unused")
public interface EleveRepository extends JpaRepository<Eleve,Long> {

}
