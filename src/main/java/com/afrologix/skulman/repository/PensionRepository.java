package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Pension;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pension entity.
 */
@SuppressWarnings("unused")
public interface PensionRepository extends JpaRepository<Pension,Long> {

}
