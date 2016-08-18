package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Utilisateur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Utilisateur entity.
 */
@SuppressWarnings("unused")
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

}
