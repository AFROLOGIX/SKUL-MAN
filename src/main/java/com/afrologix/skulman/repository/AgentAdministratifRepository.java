package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.AgentAdministratif;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AgentAdministratif entity.
 */
@SuppressWarnings("unused")
public interface AgentAdministratifRepository extends JpaRepository<AgentAdministratif,Long> {

}
