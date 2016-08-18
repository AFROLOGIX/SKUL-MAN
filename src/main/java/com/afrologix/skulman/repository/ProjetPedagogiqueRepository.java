package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.ProjetPedagogique;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjetPedagogique entity.
 */
@SuppressWarnings("unused")
public interface ProjetPedagogiqueRepository extends JpaRepository<ProjetPedagogique,Long> {

}
