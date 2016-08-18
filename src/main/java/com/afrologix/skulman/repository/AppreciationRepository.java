package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Appreciation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Appreciation entity.
 */
@SuppressWarnings("unused")
public interface AppreciationRepository extends JpaRepository<Appreciation,Long> {

}
