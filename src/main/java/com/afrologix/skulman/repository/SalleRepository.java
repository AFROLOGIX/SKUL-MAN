package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Salle;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Salle entity.
 */
@SuppressWarnings("unused")
public interface SalleRepository extends JpaRepository<Salle,Long> {

}
