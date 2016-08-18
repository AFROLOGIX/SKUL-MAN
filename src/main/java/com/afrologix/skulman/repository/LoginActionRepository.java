package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.LoginAction;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LoginAction entity.
 */
@SuppressWarnings("unused")
public interface LoginActionRepository extends JpaRepository<LoginAction,Long> {

}
