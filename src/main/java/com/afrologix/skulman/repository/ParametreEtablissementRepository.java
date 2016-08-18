package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.ParametreEtablissement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParametreEtablissement entity.
 */
@SuppressWarnings("unused")
public interface ParametreEtablissementRepository extends JpaRepository<ParametreEtablissement,Long> {

}
