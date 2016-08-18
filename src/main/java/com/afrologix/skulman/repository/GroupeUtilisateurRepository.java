package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.GroupeUtilisateur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GroupeUtilisateur entity.
 */
@SuppressWarnings("unused")
public interface GroupeUtilisateurRepository extends JpaRepository<GroupeUtilisateur,Long> {

}
