package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.PaiementPersonnel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PaiementPersonnel entity.
 */
@SuppressWarnings("unused")
public interface PaiementPersonnelRepository extends JpaRepository<PaiementPersonnel,Long> {

}
