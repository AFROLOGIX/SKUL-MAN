package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Bourse;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bourse entity.
 */
@SuppressWarnings("unused")
public interface BourseRepository extends JpaRepository<Bourse,Long> {

}
