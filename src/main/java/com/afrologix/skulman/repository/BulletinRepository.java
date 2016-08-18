package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.Bulletin;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bulletin entity.
 */
@SuppressWarnings("unused")
public interface BulletinRepository extends JpaRepository<Bulletin,Long> {

}
