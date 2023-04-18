package org.oapen.memoproject.dataingestion.harvest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;


public class ListRecordsUrlComposerTests {
	
	@Test
	public void initial_url_should_match_arguments_complete_harvest() throws MalformedURLException {
		
		ListRecordsURLComposer url = new ListRecordsURLComposer("https://www.test.com");

		String result = "";
		result = url.getUrl().toExternalForm();
		String expected = "https://www.test.com?verb=ListRecords&metadataPrefix=xoai";
		
		assertTrue(result.equals(expected));
	}
	

	@Test
	public void initial_url_should_match_arguments_from_date() throws MalformedURLException {
		
		LocalDate date1 = LocalDate.now();
		String from = date1.format(DateTimeFormatter.ISO_LOCAL_DATE);

		ListRecordsURLComposer url = new ListRecordsURLComposer("https://www.test.com");

		String result = "";
		result = url.getUrl(date1).toExternalForm();
		String expected = "https://www.test.com?verb=ListRecords&metadataPrefix=xoai&from="+from;
		
		assertTrue(result.equals(expected));
	}

	
	@Test
	public void initial_url_should_match_arguments_from_until_date() throws MalformedURLException {
		
		LocalDate date1 = LocalDate.now();
		LocalDate date2 = date1.plusDays(7);
		
		String from = date1.format(DateTimeFormatter.ISO_LOCAL_DATE);
		String until = date2.format(DateTimeFormatter.ISO_LOCAL_DATE);

		ListRecordsURLComposer url = new ListRecordsURLComposer("https://www.test.com");
		
		String result = "";
		result = url.getUrl(date1, date2).toExternalForm();
		String expected = "https://www.test.com?verb=ListRecords&metadataPrefix=xoai&from="+from+"&until="+until;
		
		assertTrue(result.equals(expected));
	}
	

	@Test
	public void resumption_url_should_match_arguments() throws MalformedURLException {
		
		ResumptionToken rst = new ResumptionToken("xoai/2022-01-21T00:00:00Z///100", 100, 0);
		
		ListRecordsURLComposer url = new ListRecordsURLComposer("https://www.test.com");

		String result = "";
		
		result = url.getUrl(rst).toExternalForm();
		String expected = "https://www.test.com?verb=ListRecords&resumptionToken="+rst.token;
		
		assertTrue(result.equals(expected));
	}
	
}
