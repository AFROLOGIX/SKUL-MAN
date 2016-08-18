package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.AbsencePersonnel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AbsencePersonnel entity.
 */
@SuppressWarnings("unused")
public interface AbsencePersonnelRepository extends JpaRepository<AbsencePersonnel,Long> {

}
