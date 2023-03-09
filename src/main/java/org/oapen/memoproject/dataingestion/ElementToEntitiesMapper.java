package org.oapen.memoproject.dataingestion;

import java.util.Optional;
import java.util.Set;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.GrantData;
import org.oapen.memoproject.dataingestion.jpa.entities.Identifier;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;

public interface ElementToEntitiesMapper {
	
	Set<Classification> getClassifications();
	Set<Contributor> getContributors();
	Set<Contribution> getContributions();
	Set<Funder> getFunders();
	Set<GrantData> getGrantData();
	Set<Identifier> getIdentifiers();
	Set<ExportChunk> getExportChunks();

	Set<String> getLanguages();
	Set<String> getSubjectsOther();

	Optional<Publisher> getPublisher();
	Optional<Integer> getYearAvailable();
	
	Optional<String> getStatus();
	Optional<String> getHandle();
	Optional<String> getSysId();
	Optional<String> getCollection();
	Optional<String> getDownloadUrl();
	Optional<String> getThumbnail();
	Optional<String> getLicense();
	Optional<String> getWebshopUrl();
	Optional<String> getDescriptionOtherLanguage();
	Optional<String> getDescriptionAbstract();
	Optional<String> getTermsAbstract();
	Optional<String> getAbstractOtherLanguage();
	Optional<String> getPartOfSeries();
	Optional<String> getTitle();
	Optional<String> getTitleAlternative();
	Optional<String> getType();
	Optional<String> getChapterNumber();
	Optional<String> getImprint();
	Optional<String> getPages();
	Optional<String> getPlacePublication();
	Optional<String> getSeriesNumber();
	Optional<String> getPartOfBook();
		
	Optional<Title> getItem();

}
