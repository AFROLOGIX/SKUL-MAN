package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Cycle;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cycle entity.
 */
@SuppressWarnings("unused")
public interface CycleRepository extends JpaRepository<Cycle,Long> {

}
