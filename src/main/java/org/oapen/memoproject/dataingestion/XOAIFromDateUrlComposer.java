package org.oapen.memoproject.dataingestion;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * A OAPEN Library XOAI request URL constructor. 
 * <p>A URL can be constructed using fromDate and number of days.
 * <p>Methods are provided to get the initial URL and URLS for subsequent pages (using a ResumptionToken). 
 * 
 * @author acdhirr
 *
 */
public final class XOAIFromDateUrlComposer implements OAIURLComposer {
	
	// https://library.oapen.org/oai/request?verb=ListRecords&metadataPrefix=xoai&from=2023-01-21
	// https://library.oapen.org/oai/request?verb=ListRecords&resumptionToken=xoai/2022-01-21T00:00:00Z///100
	
	private final String urlPath;
	private final Optional<LocalDate> fromDate; 
	private final Optional<Integer> days;
	
	public XOAIFromDateUrlComposer(String urlPath) {

		this.urlPath = urlPath;
		this.fromDate = Optional.empty();
		this.days = Optional.empty();
	}
	
	public XOAIFromDateUrlComposer(String urlPath, LocalDate fromDate ) {

		this.urlPath = urlPath;
		this.fromDate = Optional.of(fromDate);
		this.days = Optional.empty();
	}

	public XOAIFromDateUrlComposer(String urlPath, LocalDate fromDate, int days) {

		this.urlPath = urlPath;
		this.fromDate = Optional.of(fromDate);
		this.days = Optional.of(days);
	}
	
	@Override
	public URL getUrl(Optional<ResumptionToken> rst) throws MalformedURLException {
		
		if (rst.isEmpty()) return getUrl();
		else return getUrl(rst);
	}
	
	@Override
	public URL getUrl() throws MalformedURLException {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(urlPath);
		sb.append("?verb=ListRecords&metadataPrefix=xoai");
		
		if (fromDate.isPresent()) {
			
			sb.append("&from=");
			sb.append(fromDate.get().format(DateTimeFormatter.ISO_LOCAL_DATE));

			if (days.isPresent()) {
				sb.append("&until=");
				sb.append(fromDate.get().plusDays(days.get()).format(DateTimeFormatter.ISO_LOCAL_DATE));
			}	
		}
		
		return new URL(sb.toString());
	}

	@Override
	public URL getUrl(ResumptionToken rst) throws MalformedURLException {

		StringBuilder sb = new StringBuilder();
		
		sb.append(urlPath);
		sb.append("?verb=ListRecords");
		sb.append("&resumptionToken=");
		sb.append(rst.token);
		
		return new URL(sb.toString());
	}	
	
}
