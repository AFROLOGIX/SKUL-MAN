package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Serie;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Serie entity.
 */
@SuppressWarnings("unused")
public interface SerieRepository extends JpaRepository<Serie,Long> {

}
