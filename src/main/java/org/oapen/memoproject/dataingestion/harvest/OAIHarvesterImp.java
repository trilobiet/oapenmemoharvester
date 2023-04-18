package org.oapen.memoproject.dataingestion.harvest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class OAIHarvesterImp implements OAIHarvester {
	
	private final ListRecordsURLComposer urlComposer;
	private final RecordListHandler handler;
	private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	/**
	 * @param urlPath OAI path without query string, e.g. <em>https://library.oapen.org/oai/request</em>
	 */
	public OAIHarvesterImp(ListRecordsURLComposer url, RecordListHandler handler) {
		
		this.urlComposer = url;
		this.handler = handler;
	}


	@Override
	public final List<String> harvest() throws HarvestException {
		
		URL nextUrl = urlComposer.getUrl();
		return harvest(nextUrl);
	}
	
	
	@Override
	public final List<String> harvest(LocalDate fromDate, LocalDate untilDate) throws HarvestException {
		
		URL nextUrl = urlComposer.getUrl(fromDate,untilDate);
		return harvest(nextUrl);
	}
	

	@Override
	public final List<String> harvest(String token) throws HarvestException {
		
		ResumptionToken rst = new ResumptionToken(token, null, null);
		URL nextUrl = urlComposer.getUrl(rst);
		return harvest(nextUrl);
	}
	
	
	private List<String> harvest(URL url) throws HarvestException {
		
		Optional<ResumptionToken> oRst = Optional.empty();
		
		Document downloadedDoc;
		
		List<String> insertedHandles = new ArrayList<String>();
		
		do {

			try {
				
				if(oRst.isPresent()) url = urlComposer.getUrl(oRst.get());
				
				downloadedDoc = fetchDocument(url);
				
				ListRecordsDocumentImp lrDocument = new ListRecordsDocumentImp(downloadedDoc);
				
				List<Element> records = lrDocument.getRecords();
				
				insertedHandles.addAll(handler.process(records));
				
				oRst = lrDocument.getResumptionToken();
	
				Thread.sleep(500); // Do not DDOS the OAI Provider
				
			} catch (Exception e) {	
				throw new HarvestException(e, oRst);
			} 
			
		} while ( oRst.isPresent() );
		
		return insertedHandles;
		
	}
	
	
	private final Document fetchDocument(URL url) throws IOException, ParserConfigurationException, SAXException  {

		URLConnection urlConnection = url.openConnection();
		
		urlConnection.addRequestProperty("Accept", "application/xml");
		
		Document document;
		
		try( InputStream is = urlConnection.getInputStream() ) {
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(is);

			return document;
		} 
		
	}
	

}

