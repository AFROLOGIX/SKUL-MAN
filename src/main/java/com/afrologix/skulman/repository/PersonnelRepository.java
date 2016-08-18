package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Personnel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Personnel entity.
 */
@SuppressWarnings("unused")
public interface PersonnelRepository extends JpaRepository<Personnel,Long> {

}
