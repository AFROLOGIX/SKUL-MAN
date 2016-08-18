package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.MoyenneTableauHonneur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MoyenneTableauHonneur entity.
 */
@SuppressWarnings("unused")
public interface MoyenneTableauHonneurRepository extends JpaRepository<MoyenneTableauHonneur,Long> {

}
