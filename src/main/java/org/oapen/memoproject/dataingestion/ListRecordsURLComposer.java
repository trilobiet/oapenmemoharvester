package org.oapen.memoproject.dataingestion;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Returns a URL for a (OAI) repository, optionally given a ResumptionToken.
 * 
 * @author acdhirr
 *
 */
public interface ListRecordsURLComposer {

	URL getUrl() throws MalformedURLException;
	
	URL getUrl(ResumptionToken rst) throws MalformedURLException;
	
	URL getUrl(Optional<ResumptionToken> rst) throws MalformedURLException;
}
