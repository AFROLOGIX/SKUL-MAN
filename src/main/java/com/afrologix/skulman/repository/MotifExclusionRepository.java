package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.MotifExclusion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MotifExclusion entity.
 */
@SuppressWarnings("unused")
public interface MotifExclusionRepository extends JpaRepository<MotifExclusion,Long> {

}
