package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Fonctionnalite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Fonctionnalite entity.
 */
@SuppressWarnings("unused")
public interface FonctionnaliteRepository extends JpaRepository<Fonctionnalite,Long> {

}
