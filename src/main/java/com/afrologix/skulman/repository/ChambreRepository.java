package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Chambre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Chambre entity.
 */
@SuppressWarnings("unused")
public interface ChambreRepository extends JpaRepository<Chambre,Long> {

}
