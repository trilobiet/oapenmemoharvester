package org.oapen.memoproject.dataingestion.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaPersistenceService implements PersistenceService {
	
	@Autowired
	TitleRepository titleRepository;

	@Autowired
	ContributorRepository contributorRepository;
	
	@Autowired
	PublisherRepository publisherRepository;

	@Autowired
	FunderRepository funderRepository;
	
	@Autowired
	ClassificationRepository classificationRepository;

	@Autowired
	ExportChunkRepository exportChunkRepository;
	
	private static final Logger logger = 
			LoggerFactory.getLogger(JpaPersistenceService.class);
	

	@Override
	public List<Funder> saveFunders(Set<Funder> funders) {
		
		List<Funder> r = new ArrayList<>();
		
		if (funders != null) try {
			r = funderRepository.saveAll(funders);
		}
		catch (Exception e) {
			logger.error("Funders: " + e.getMessage());
			throw new RuntimeException(e);
		}
		return r;
	}
	

	@Override
	public Optional<Publisher> savePublisher(Publisher publisher) {
		
		Optional<Publisher> r = Optional.empty();
		
		if (publisher != null) try {
			r = Optional.of(publisherRepository.save(publisher));
		}
		catch (Exception e) {
			logger.error("Publisher: " + e.getMessage());
			throw new RuntimeException(e);
		}
		return r;
	}

	
	@Override
	public List<Contributor> saveContributors(Set<Contributor> contributors) {
		
		List<Contributor> r = new ArrayList<>();
		
		if (contributors != null) try {
			r = contributorRepository.saveAll(contributors);
		}
		catch (Exception e) {
			logger.error("Contributors: " + e.getMessage());
			throw new RuntimeException(e);
		}
		return r;
	}
	

	@Override
	public List<Classification> saveClassifications(Set<Classification> classifications) {
		
		List<Classification> r = new ArrayList<>();
		
		if (classifications != null) try {
			r = classificationRepository.saveAll(classifications);
		}
		catch (Exception e) {
			logger.error("Classifications: " + e.getMessage());
			throw new RuntimeException(e);
		}
		return r;
	}
	

	@Override
	public Optional<Title> saveTitle(Title title) {
		
		Optional<Title> r = Optional.empty();

		if (title != null) try {
			r = Optional.of(titleRepository.save(title));
		}
		catch (Exception e) {
			logger.error("Title " + title.getHandle() + ": " + e.getMessage());
			throw new RuntimeException(e);
		}
		
		return r;
	}
	
	
	@Override
	public List<ExportChunk> saveExportChunks(Set<ExportChunk> exportChunks) {
		
		List<ExportChunk> r = new ArrayList<>();
		
		try {
			r = exportChunkRepository.saveAll(exportChunks);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return r;
	}

	
	@Override
	public void deleteTitle(Title title) {
		
		try {
			titleRepository.delete(title);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	
	@Override
	public void deleteAll() {
		
		try {
			titleRepository.deleteAll();
			classificationRepository.deleteAll();
			contributorRepository.deleteAll();
			funderRepository.deleteAll();
			publisherRepository.deleteAll();
			exportChunkRepository.deleteAll();
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
	}


	
}
