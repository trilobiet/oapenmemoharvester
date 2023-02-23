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

public interface ElementToEntitiesMapper {
	
	Set<Classification> getClassifications() throws MappingException;
	Set<Contributor> getContributors() throws MappingException;
	Set<Contribution> getContributions() throws MappingException;
	Set<Funder> getFunders() throws MappingException;
	Set<GrantData> getGrantData() throws MappingException;
	Set<Identifier> getIdentifiers() throws MappingException;
	Set<ExportChunk> getExportChunks() throws MappingException;

	Set<String> getLanguages() throws MappingException;
	Set<String> getSubjectsOther() throws MappingException;
	Set<String> getDatesAccessioned() throws MappingException;

	Optional<Publisher> getPublisher() throws MappingException;
	
	Optional<String> getHandle() throws MappingException;
	Optional<String> getSysId() throws MappingException;
	Optional<String> getCollection() throws MappingException;
	Optional<String> getDownloadUrl() throws MappingException;
	Optional<String> getThumbnail() throws MappingException;
	Optional<String> getLicense() throws MappingException;
	Optional<String> getWebshopUrl() throws MappingException;
	Optional<String> getDateAvailable() throws MappingException;
	Optional<String> getDateIssued() throws MappingException;
	Optional<String> getDescription() throws MappingException;
	Optional<String> getDescriptionOtherLanguage() throws MappingException;
	Optional<String> getDescriptionAbstract() throws MappingException;
	Optional<String> getDescriptionProvenance() throws MappingException;
	Optional<String> getTermsAbstract() throws MappingException;
	Optional<String> getAbstractOtherLanguage() throws MappingException;
	Optional<String> getPartOfSeries() throws MappingException;
	Optional<String> getTitle() throws MappingException;
	Optional<String> getTitleAlternative() throws MappingException;
	Optional<String> getType() throws MappingException;
	Optional<String> getChapterNumber() throws MappingException;
	Optional<String> getEmbargo() throws MappingException;
	Optional<String> getImprint() throws MappingException;
	Optional<String> getPages() throws MappingException;
	Optional<String> getPlacePublication() throws MappingException;
	Optional<String> getSeriesNumber() throws MappingException;
	Optional<String> getPartOfBook() throws MappingException;

}
