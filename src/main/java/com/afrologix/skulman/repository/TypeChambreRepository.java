package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TypeChambre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeChambre entity.
 */
@SuppressWarnings("unused")
public interface TypeChambreRepository extends JpaRepository<TypeChambre,Long> {

}
