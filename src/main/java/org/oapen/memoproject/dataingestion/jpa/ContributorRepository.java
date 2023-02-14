package org.oapen.memoproject.dataingestion.jpa;

import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

interface ContributorRepository extends JpaRepository<Contributor, String> {

}
