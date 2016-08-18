package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Cours;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cours entity.
 */
@SuppressWarnings("unused")
public interface CoursRepository extends JpaRepository<Cours,Long> {

}
