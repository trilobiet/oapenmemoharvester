package org.oapen.memoproject.dataingestion.jpa;

import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.springframework.data.jpa.repository.JpaRepository;

interface TitleRepository extends JpaRepository<Title, String> {

}
