package org.oapen.memoproject.dataingestion;

import java.util.Set;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.Identifier;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;

public interface ElementToEntitiesMapper {
	
	Set<Classification> getClassifications() throws MappingException;
	Set<Contributor> getContributors() throws MappingException;
	Set<Funder> getFunders() throws MappingException;
	Set<Funder> getFundings() throws MappingException;
	Set<Identifier> getIdentifiers() throws MappingException;
	Set<Contribution> getContributions() throws MappingException;
	Set<ExportChunk> getExportChunks() throws MappingException;

	Set<String> getLanguages() throws MappingException;
	Set<String> getSubjectsOther() throws MappingException;
	Set<String> getDatesAccessioned() throws MappingException;

	Publisher getPublisher() throws MappingException;
	
	String getHandle() throws MappingException;
	String getSysId() throws MappingException;
	String getCollection() throws MappingException;
	String getDownloadUrl() throws MappingException;
	String getThumbnail() throws MappingException;
	String getLicense() throws MappingException;
	String getWebshopUrl() throws MappingException;
	String getDateAvailable() throws MappingException;
	String getDateIssued() throws MappingException;
	String getDescription() throws MappingException;
	String getDescriptionOtherLanguage() throws MappingException;
	String getDescriptionAbstract() throws MappingException;
	String getDescriptionProvenance() throws MappingException;
	String getTermsAbstract() throws MappingException;
	String getAbstractOtherLanguage() throws MappingException;
	String getPartOfSeries() throws MappingException;
	String getTitle() throws MappingException;
	String getTitleAlternative() throws MappingException;
	String getType() throws MappingException;
	String getChapterNumber() throws MappingException;
	String getEmbargo() throws MappingException;
	String getImprint() throws MappingException;
	String getPages() throws MappingException;
	String getPlacePublication() throws MappingException;
	String getSeriesNumber() throws MappingException;
	String getPartOfBook() throws MappingException;

}
