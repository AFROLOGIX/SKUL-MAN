package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Sequence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sequence entity.
 */
@SuppressWarnings("unused")
public interface SequenceRepository extends JpaRepository<Sequence,Long> {

}
