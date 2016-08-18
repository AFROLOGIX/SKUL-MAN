package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Niveau;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Niveau entity.
 */
@SuppressWarnings("unused")
public interface NiveauRepository extends JpaRepository<Niveau,Long> {

}
