package org.oapen.memoproject.dataingestion.harvest;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	public final void harvest() throws HarvestException {
		
		Optional<ResumptionToken> oRst = Optional.empty();
		
		Document downloadedDoc;
		
		do {

			try {
				
				URL nextUrl = urlComposer.getUrl(oRst);
				
				downloadedDoc = fetchDocument(nextUrl);
				
				ListRecordsDocumentImp lrDocument = new ListRecordsDocumentImp(downloadedDoc);
				
				List<Element> records = lrDocument.getRecords();
				
				List<String> insertedHandles = handler.process(records);
				
				oRst = lrDocument.getResumptionToken();
	
				Thread.sleep(500); // Do not DDOS the OAI Provider
				
			} catch (Exception e) {	throw new HarvestException(e);} 
			
		} while ( oRst.isPresent() );
		
	}
	
	
	@Override
	public final Document fetchDocument(URL url) throws Exception  {

		URLConnection urlConnection = url.openConnection();
		
		urlConnection.addRequestProperty("Accept", "application/xml");
		
		Document document;
		
		try( InputStream is = urlConnection.getInputStream() ) {
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(is);

			return document;
			
		} catch (Exception e) {
			
			throw(e);
		}
		
	}
	

}

