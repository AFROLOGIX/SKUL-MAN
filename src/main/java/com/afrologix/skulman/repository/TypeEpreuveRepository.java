package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TypeEpreuve;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeEpreuve entity.
 */
@SuppressWarnings("unused")
public interface TypeEpreuveRepository extends JpaRepository<TypeEpreuve,Long> {

}
