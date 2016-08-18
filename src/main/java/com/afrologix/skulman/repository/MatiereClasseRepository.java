package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.MatiereClasse;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MatiereClasse entity.
 */
@SuppressWarnings("unused")
public interface MatiereClasseRepository extends JpaRepository<MatiereClasse,Long> {

}
