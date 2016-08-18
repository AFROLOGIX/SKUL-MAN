package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Trimestre;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Trimestre entity.
 */
@SuppressWarnings("unused")
public interface TrimestreRepository extends JpaRepository<Trimestre,Long> {

}
