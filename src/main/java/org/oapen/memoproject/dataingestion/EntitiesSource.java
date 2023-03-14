package org.oapen.memoproject.dataingestion;

import java.util.Optional;
import java.util.Set;

import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;

public interface EntitiesSource {
	
	Set<Classification> getClassifications();
	Set<Contributor> getContributors();
	Set<Funder> getFunders();
	Optional<Publisher> getPublisher();
	Optional<Title> getTitle();
}
