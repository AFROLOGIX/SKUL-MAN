package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TarifBus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TarifBus entity.
 */
@SuppressWarnings("unused")
public interface TarifBusRepository extends JpaRepository<TarifBus,Long> {

}
