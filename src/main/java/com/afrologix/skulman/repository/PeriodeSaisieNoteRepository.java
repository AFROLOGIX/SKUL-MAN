package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.PeriodeSaisieNote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PeriodeSaisieNote entity.
 */
@SuppressWarnings("unused")
public interface PeriodeSaisieNoteRepository extends JpaRepository<PeriodeSaisieNote,Long> {

}
