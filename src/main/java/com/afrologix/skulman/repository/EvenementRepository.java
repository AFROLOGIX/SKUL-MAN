package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Evenement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Evenement entity.
 */
@SuppressWarnings("unused")
public interface EvenementRepository extends JpaRepository<Evenement,Long> {

}
