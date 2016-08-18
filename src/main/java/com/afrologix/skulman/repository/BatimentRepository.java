package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Batiment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Batiment entity.
 */
@SuppressWarnings("unused")
public interface BatimentRepository extends JpaRepository<Batiment,Long> {

}
