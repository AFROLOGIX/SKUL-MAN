package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Moratoire;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Moratoire entity.
 */
@SuppressWarnings("unused")
public interface MoratoireRepository extends JpaRepository<Moratoire,Long> {

}
