package org.oapen.memoproject.dataingestion.jpa;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;

public interface PersistenceService {

	List<Classification> saveClassifications(Set<Classification> classifications);
	List<Contributor> saveContributors(Set<Contributor> contributors);
	List<Funder> saveFunders(Set<Funder> funders);
	List<ExportChunk> saveExportChunks(Set<ExportChunk> exportChunks);

	Optional<Publisher> savePublisher(Publisher publisher);
	Optional<Title> saveTitle(Title title);
	
	void deleteTitle(Title title);
	void deleteAll();
	
	List<ExportChunk> getExportChunks(String handle);
}


