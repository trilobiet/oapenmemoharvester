package org.oapen.memoproject.dataingestion.jpa;

import java.util.List;

import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.springframework.data.jpa.repository.JpaRepository;

interface ExportChunkRepository extends JpaRepository<ExportChunk, String> {
	
	// JPA Magic: creates where clause "where handle_title = ?" 
    List<ExportChunk> findByHandleTitle(String handle);
	
}
