package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.EnseignantTitulaire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EnseignantTitulaire entity.
 */
@SuppressWarnings("unused")
public interface EnseignantTitulaireRepository extends JpaRepository<EnseignantTitulaire,Long> {

}
