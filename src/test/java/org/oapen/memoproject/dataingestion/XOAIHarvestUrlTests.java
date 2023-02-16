package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;


public class XOAIHarvestUrlTests {
	
	@Test
	public void initial_url_should_match_arguments_complete_harvest() {
		
		XOAIHarvestUrl url = new XOAIHarvestUrl("https://www.test.com");

		String result = "";
		try { result = url.getUrl().toExternalForm(); } catch (MalformedURLException e) {}
		String expected = "https://www.test.com?verb=ListRecords&metadataPrefix=xoai";
		
		assertTrue(result.equals(expected));
	}
	

	@Test
	public void initial_url_should_match_arguments_from_date() {
		
		LocalDate date = LocalDate.now();
		String from = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

		XOAIHarvestUrl url = new XOAIHarvestUrl("https://www.test.com", date);

		String result = "";
		try { result = url.getUrl().toExternalForm(); } catch (MalformedURLException e) {}
		String expected = "https://www.test.com?verb=ListRecords&metadataPrefix=xoai&from="+from;
		
		assertTrue(result.equals(expected));
	}

	@Test
	public void initial_url_should_match_arguments_from_until_date() {
		
		LocalDate date = LocalDate.now();
		int days = 7;
		
		String from = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String until = date.plusDays(days).format(DateTimeFormatter.ISO_LOCAL_DATE);

		XOAIHarvestUrl url = new XOAIHarvestUrl("https://www.test.com", date, days);
		
		String result = "";
		try { result = url.getUrl().toExternalForm(); } catch (MalformedURLException e) {}
		String expected = "https://www.test.com?verb=ListRecords&metadataPrefix=xoai&from="+from+"&until="+until;
		
		assertTrue(result.equals(expected));
	}
	

	@Test
	public void resumption_url_should_match_arguments() {
		
		LocalDate date = LocalDate.now();
		int days = 7;
		String resumptionToken = "xoai/2022-01-21T00:00:00Z///100";
		
		XOAIHarvestUrl url = new XOAIHarvestUrl("https://www.test.com", date, days);

		String result = "";
		try { result = url.getUrl(resumptionToken).toExternalForm(); } catch (MalformedURLException e) {}
		String expected = "https://www.test.com?verb=ListRecords&resumptionToken="+resumptionToken;
		
		assertTrue(result.equals(expected));
	}
	
	

}
