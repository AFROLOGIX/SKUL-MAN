package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TypePersonnel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypePersonnel entity.
 */
@SuppressWarnings("unused")
public interface TypePersonnelRepository extends JpaRepository<TypePersonnel,Long> {

}
