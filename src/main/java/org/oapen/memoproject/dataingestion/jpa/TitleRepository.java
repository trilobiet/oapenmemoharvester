package org.oapen.memoproject.dataingestion.jpa;

import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.springframework.data.jpa.repository.JpaRepository;

interface TitleRepository extends JpaRepository<Title, String> {
	
	 //@Query("SELECT t FROM Title t WHERE t.id = ?1")
	 //Title findByHandle(String id);

}
