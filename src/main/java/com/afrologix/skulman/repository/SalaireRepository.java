package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Salaire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Salaire entity.
 */
@SuppressWarnings("unused")
public interface SalaireRepository extends JpaRepository<Salaire,Long> {

}
