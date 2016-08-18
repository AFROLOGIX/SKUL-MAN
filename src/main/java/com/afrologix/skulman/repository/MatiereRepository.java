package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Matiere;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Matiere entity.
 */
@SuppressWarnings("unused")
public interface MatiereRepository extends JpaRepository<Matiere,Long> {

}
