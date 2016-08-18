package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Parent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Parent entity.
 */
@SuppressWarnings("unused")
public interface ParentRepository extends JpaRepository<Parent,Long> {

}
