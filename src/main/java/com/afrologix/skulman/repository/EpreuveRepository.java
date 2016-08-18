package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Epreuve;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Epreuve entity.
 */
@SuppressWarnings("unused")
public interface EpreuveRepository extends JpaRepository<Epreuve,Long> {

}
