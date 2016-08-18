package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Vacation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vacation entity.
 */
@SuppressWarnings("unused")
public interface VacationRepository extends JpaRepository<Vacation,Long> {

}
