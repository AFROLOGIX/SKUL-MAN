package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.FormatMatricule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FormatMatricule entity.
 */
@SuppressWarnings("unused")
public interface FormatMatriculeRepository extends JpaRepository<FormatMatricule,Long> {

}
