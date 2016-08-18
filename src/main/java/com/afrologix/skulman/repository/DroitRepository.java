package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Droit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Droit entity.
 */
@SuppressWarnings("unused")
public interface DroitRepository extends JpaRepository<Droit,Long> {

}
