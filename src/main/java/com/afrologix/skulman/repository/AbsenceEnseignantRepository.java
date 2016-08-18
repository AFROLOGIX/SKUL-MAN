package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.AbsenceEnseignant;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AbsenceEnseignant entity.
 */
@SuppressWarnings("unused")
public interface AbsenceEnseignantRepository extends JpaRepository<AbsenceEnseignant,Long> {

}
