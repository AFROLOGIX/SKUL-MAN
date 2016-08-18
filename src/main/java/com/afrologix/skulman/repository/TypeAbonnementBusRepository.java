package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TypeAbonnementBus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeAbonnementBus entity.
 */
@SuppressWarnings("unused")
public interface TypeAbonnementBusRepository extends JpaRepository<TypeAbonnementBus,Long> {

}
