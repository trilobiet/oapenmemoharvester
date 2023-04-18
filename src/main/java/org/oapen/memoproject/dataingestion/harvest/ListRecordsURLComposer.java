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
	
	private final URL url;
	
	public ListRecordsURLComposer(String urlPath) throws MalformedURLException {

		this.url = new URL(urlPath);
	}
	
	public URL getUrl() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(url.toString());
		sb.append("?verb=ListRecords&metadataPrefix=xoai");

		try {
			return new URL(sb.toString());
		} catch (MalformedURLException e) {
			// this will never happen since we tested the URL already in the constructor
			return null;
		}
	}

	public URL getUrl(LocalDate fromDate) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(url.toString());
		sb.append("?verb=ListRecords&metadataPrefix=xoai");
		
		sb.append("&from=");
		sb.append(fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

		try {
			return new URL(sb.toString());
		} catch (MalformedURLException e) {
			// this will never happen since we tested the URL already in the constructor
			return null;
		}
	}
	
	
	public URL getUrl(LocalDate fromDate, LocalDate untilDate) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(url.toString());
		sb.append("?verb=ListRecords&metadataPrefix=xoai");
		
		sb.append("&from=");
		sb.append(fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

		sb.append("&until=");
		sb.append(untilDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
		
		try {
			return new URL(sb.toString());
		} catch (MalformedURLException e) {
			// this will never happen since we tested the URL already in the constructor
			return null;
		}
	}

	public URL getUrl(ResumptionToken rst) {

		StringBuilder sb = new StringBuilder();
		
		sb.append(url.toString());
		sb.append("?verb=ListRecords");
		sb.append("&resumptionToken=");
		sb.append(rst.token);
		
		try {
			return new URL(sb.toString());
		} catch (MalformedURLException e) {
			// this will never happen since we tested the URL already in the constructor
			return null;
		}
	}	
	
}
