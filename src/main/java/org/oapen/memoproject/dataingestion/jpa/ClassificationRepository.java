package org.oapen.memoproject.dataingestion.jpa;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.springframework.data.jpa.repository.JpaRepository;

interface ClassificationRepository extends JpaRepository<Classification, String> {

}
