package org.oapen.memoproject.dataingestion.jpa;

import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.springframework.data.jpa.repository.JpaRepository;

interface FunderRepository extends JpaRepository<Funder, String> {

}
