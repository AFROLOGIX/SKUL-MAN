package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Fichier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fichier entity.
 */
@SuppressWarnings("unused")
public interface FichierRepository extends JpaRepository<Fichier,Long> {

}
