package org.oapen.memoproject.dataingestion.harvest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A OAPEN Library XOAI request URL constructor. 
 * <p>A URL can be constructed using fromDate and number of days.
 * <p>Methods are provided to get the initial URL and URLS for subsequent pages (using a ResumptionToken). 
 * 
 * @author acdhirr
 *
 */
public final class ListRecordsURLComposer  {
	
	// https://library.oapen.org/oai/request?verb=ListRecords&metadataPrefix=xoai&from=2023-01-21
	// https://library.oapen.org/oai/request?verb=ListRecords&resumptionToken=xoai/2022-01-21T00:00:00Z///100
	
	private final String urlPath;
	
	public ListRecordsURLComposer(String urlPath) {

		this.urlPath = urlPath;
	}
	
	public URL getUrl() throws MalformedURLException {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(urlPath);
		sb.append("?verb=ListRecords&metadataPrefix=xoai");
		
		return new URL(sb.toString());
	}

	public URL getUrl(LocalDate fromDate) throws MalformedURLException {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(urlPath);
		sb.append("?verb=ListRecords&metadataPrefix=xoai");
		
		sb.append("&from=");
		sb.append(fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

		return new URL(sb.toString());
	}
	
	
	public URL getUrl(LocalDate fromDate, LocalDate untilDate) throws MalformedURLException {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(urlPath);
		sb.append("?verb=ListRecords&metadataPrefix=xoai");
		
		sb.append("&from=");
		sb.append(fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

		sb.append("&until=");
		sb.append(untilDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
		
		return new URL(sb.toString());
	}

	public URL getUrl(ResumptionToken rst) throws MalformedURLException {

		StringBuilder sb = new StringBuilder();
		
		sb.append(urlPath);
		sb.append("?verb=ListRecords");
		sb.append("&resumptionToken=");
		sb.append(rst.token);
		
		return new URL(sb.toString());
	}	
	
}
