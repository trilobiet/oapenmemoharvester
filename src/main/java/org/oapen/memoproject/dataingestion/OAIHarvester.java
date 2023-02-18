package org.oapen.memoproject.dataingestion;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.javatuples.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OAIHarvester {
	
	private final OAIURLComposer harvestUrl;
	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	/**
	 * @param urlPath OAI path without query string, e.g. <em>https://library.oapen.org/oai/request</em>
	 */
	public OAIHarvester(OAIURLComposer url) {
		
		harvestUrl = url;
	}
	
	/**
	 * Start harvesting. Harvesting will continue until no more ResumptionTokens are encountered.
	 * The handler will be invoked for each harvested page. When there are x ResumptionTokens,
	 * handler will be invoked x + 1 times. 
	 * 
	 * @param handler Handler function to process each Document 
	 * @throws HarvestException
	 */
	public void harvest(OAIDocumentHandler handler) throws HarvestException {
		
		Optional<ResumptionToken> oRst = Optional.empty();
		
		Document doc;
		
		do {

			try {

				Pair<Document, Optional<ResumptionToken>> page;
				page = getPage(harvestUrl.getUrl(oRst));
				
				doc = page.getValue0();
				oRst = page.getValue1();
	
				handler.process(doc);
	
				Thread.sleep(1000);
				
			} catch (Exception e) {	throw new HarvestException(e);} 
			
		} while ( oRst.isPresent() );
		
	}
	
	
	/**
	 * Load an OAI XML document and parse its optional ResumptionToken
	 * 	 * 
	 * @param url
	 * @return A Pair containing the Document and an optional ResumptionToken
	 * @throws Exception
	 */
	private Pair<Document,Optional<ResumptionToken>> getPage(URL url) throws Exception  {

		URLConnection urlConnection = url.openConnection();
		
		urlConnection.addRequestProperty("Accept", "application/xml");
		
		Document document;
		
		try( InputStream is = urlConnection.getInputStream() ) {
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(is);

			Optional<ResumptionToken> resumptionToken = getResumptionToken(document);
			
			return Pair.with(document, resumptionToken);
			
		} catch (Exception e) {
			
			throw(e);
		}
		
	}
	
	
	/** 
	 * Get a ResumptionToken from a Document, if present
	 * 
	 * @param doc the Document
	 * @return an Optional ResumptionToken
	 */
	private Optional<ResumptionToken> getResumptionToken(Document doc) {
		
		NodeList nl = doc.getElementsByTagName("resumptionToken");
		
		if (nl.getLength() > 0) {
			
			Node node = nl.item(0);
			
			ResumptionToken rt = new ResumptionToken(
				node.getTextContent(),
				node.getAttributes().getNamedItem("completeListSize").getTextContent(),
				node.getAttributes().getNamedItem("cursor").getTextContent()
			);
			
			if (node.getTextContent().isEmpty()) return Optional.empty();
			else return Optional.of(rt);
		}
		else return Optional.empty();
	}
	

}

