package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Pause;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pause entity.
 */
@SuppressWarnings("unused")
public interface PauseRepository extends JpaRepository<Pause,Long> {

}
