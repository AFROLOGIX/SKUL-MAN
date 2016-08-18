package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TypeTrancheHoraire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeTrancheHoraire entity.
 */
@SuppressWarnings("unused")
public interface TypeTrancheHoraireRepository extends JpaRepository<TypeTrancheHoraire,Long> {

}
