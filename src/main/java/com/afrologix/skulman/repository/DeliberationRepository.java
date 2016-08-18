package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Deliberation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Deliberation entity.
 */
@SuppressWarnings("unused")
public interface DeliberationRepository extends JpaRepository<Deliberation,Long> {

}
