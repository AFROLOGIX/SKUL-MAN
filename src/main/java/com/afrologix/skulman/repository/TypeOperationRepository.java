package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TypeOperation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeOperation entity.
 */
@SuppressWarnings("unused")
public interface TypeOperationRepository extends JpaRepository<TypeOperation,Long> {

}
