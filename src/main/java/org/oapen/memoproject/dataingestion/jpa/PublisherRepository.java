package org.oapen.memoproject.dataingestion.jpa;

import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

interface PublisherRepository extends JpaRepository<Publisher, String> {

}
