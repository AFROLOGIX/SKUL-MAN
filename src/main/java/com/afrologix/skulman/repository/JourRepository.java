package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Jour;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Jour entity.
 */
@SuppressWarnings("unused")
public interface JourRepository extends JpaRepository<Jour,Long> {

}
