package org.oapen.memoproject.dataingestion.jpa;

import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.springframework.data.jpa.repository.JpaRepository;

interface ExportChunkRepository extends JpaRepository<ExportChunk, String> {
	
}
