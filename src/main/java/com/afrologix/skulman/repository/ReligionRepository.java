package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Religion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Religion entity.
 */
@SuppressWarnings("unused")
public interface ReligionRepository extends JpaRepository<Religion,Long> {

}
