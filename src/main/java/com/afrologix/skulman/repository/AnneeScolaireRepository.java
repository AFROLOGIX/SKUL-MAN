package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.AnneeScolaire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AnneeScolaire entity.
 */
@SuppressWarnings("unused")
public interface AnneeScolaireRepository extends JpaRepository<AnneeScolaire,Long> {

}
