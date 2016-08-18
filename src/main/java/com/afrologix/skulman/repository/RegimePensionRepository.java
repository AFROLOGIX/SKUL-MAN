package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.RegimePension;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RegimePension entity.
 */
@SuppressWarnings("unused")
public interface RegimePensionRepository extends JpaRepository<RegimePension,Long> {

}
