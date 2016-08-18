package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.VersementEleve;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VersementEleve entity.
 */
@SuppressWarnings("unused")
public interface VersementEleveRepository extends JpaRepository<VersementEleve,Long> {

}
