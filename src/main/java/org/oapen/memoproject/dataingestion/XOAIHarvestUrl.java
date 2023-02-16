package org.oapen.memoproject.dataingestion;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class XOAIHarvestUrl {
	
	// https://library.oapen.org/oai/request?verb=ListRecords&metadataPrefix=xoai&from=2023-01-21
	// https://library.oapen.org/oai/request?verb=ListRecords&resumptionToken=xoai/2022-01-21T00:00:00Z///100
	
	private final String baseUrl;
	private final Optional<LocalDate> fromDate; 
	private final Optional<Integer> days;
	
	public XOAIHarvestUrl(String baseUrl) {
		super();
		this.baseUrl = baseUrl;
		this.fromDate = Optional.empty();
		this.days = Optional.empty();
	}
	
	public XOAIHarvestUrl(String baseUrl, LocalDate fromDate ) {
		super();
		this.baseUrl = baseUrl;
		this.fromDate = Optional.of(fromDate);
		this.days = Optional.empty();
	}

	public XOAIHarvestUrl(String baseUrl, LocalDate fromDate, int days) {
		super();
		this.baseUrl = baseUrl;
		this.fromDate = Optional.of(fromDate);
		this.days = Optional.of(days);
	}
	
	public URL getUrl() throws MalformedURLException {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(baseUrl);
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

	public URL getUrl(String resumptionToken) throws MalformedURLException {

		StringBuilder sb = new StringBuilder();
		
		sb.append(baseUrl);
		sb.append("?verb=ListRecords");
		sb.append("&resumptionToken=");
		sb.append(resumptionToken);
		
		return new URL(sb.toString());
	}	
	
}
