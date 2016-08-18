package com.afrologix.skulman.repository;

import com.afrologix.skulman.domain.OptionBulletinNote;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OptionBulletinNote entity.
 */
@SuppressWarnings("unused")
public interface OptionBulletinNoteRepository extends JpaRepository<OptionBulletinNote,Long> {

}
