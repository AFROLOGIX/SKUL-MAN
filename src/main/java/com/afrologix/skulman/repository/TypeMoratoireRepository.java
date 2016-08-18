package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TypeMoratoire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeMoratoire entity.
 */
@SuppressWarnings("unused")
public interface TypeMoratoireRepository extends JpaRepository<TypeMoratoire,Long> {

}
