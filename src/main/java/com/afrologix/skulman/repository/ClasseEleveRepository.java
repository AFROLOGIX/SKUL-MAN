package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.ClasseEleve;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClasseEleve entity.
 */
@SuppressWarnings("unused")
public interface ClasseEleveRepository extends JpaRepository<ClasseEleve,Long> {

}
