package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Section;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Section entity.
 */
@SuppressWarnings("unused")
public interface SectionRepository extends JpaRepository<Section,Long> {

}
