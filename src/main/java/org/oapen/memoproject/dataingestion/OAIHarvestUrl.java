package org.oapen.memoproject.dataingestion;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public interface OAIHarvestUrl {

	
	URL getUrl(Optional<ResumptionToken> rst) throws MalformedURLException;

	
	URL getUrl() throws MalformedURLException;
}
