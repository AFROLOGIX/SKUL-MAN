package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.LoginConnexion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LoginConnexion entity.
 */
@SuppressWarnings("unused")
public interface LoginConnexionRepository extends JpaRepository<LoginConnexion,Long> {

}
