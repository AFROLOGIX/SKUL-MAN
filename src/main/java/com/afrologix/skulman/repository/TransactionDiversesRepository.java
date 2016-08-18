package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.TransactionDiverses;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TransactionDiverses entity.
 */
@SuppressWarnings("unused")
public interface TransactionDiversesRepository extends JpaRepository<TransactionDiverses,Long> {

}
