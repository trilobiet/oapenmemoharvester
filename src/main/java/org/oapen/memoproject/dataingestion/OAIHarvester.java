package org.oapen.memoproject.dataingestion;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.javatuples.Pair;
import org.javatuples.Tuple;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OAIHarvester {
	
	private final XOAIHarvestUrl harvestUrl;
	
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	
	public OAIHarvester(String path) {
		
		harvestUrl = new XOAIHarvestUrl(path);
	}

	public OAIHarvester(String path, LocalDate fromDate) {
		
		harvestUrl = new XOAIHarvestUrl(path, fromDate);
	}
	
	public OAIHarvester(String path, LocalDate fromDate, int days) {
		
		harvestUrl = new XOAIHarvestUrl(path, fromDate, days);
	}

	
	public void harvest() throws Exception {
		
		Pair<Document, Optional<ResumptionToken>> page = getPage(harvestUrl.getUrl());
		
		Optional<ResumptionToken> oRst = page.getValue1();
		
		while( oRst.isPresent() ) {
			
			System.out.println(oRst.get());
			
			Thread.sleep(1000);
			
			ResumptionToken rst = oRst.get();
			page = getPage(harvestUrl.getUrl(rst.token));

			oRst = page.getValue1();
		}
		
	}
	
	
	
	
	public Pair<Document,Optional<ResumptionToken>> getPage(URL url) throws Exception  {

		Document document = getXml(url);

		Optional<ResumptionToken> resumptionToken = getResumptionToken(document);
		
		return Pair.with(document, resumptionToken);
		
		
		/*	
		
		try (InputStream is = getInputStream(harvestUrl.getUrl())) {
			
			
			Path path = Path.of(filePath);
			Path parent = path.getParent();
			Files.createDirectories(parent);
			File tmp = File.createTempFile("tmp", "._ml", parent.toFile()); 
			Files.copy(is, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.move(tmp.toPath(), path, 
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.ATOMIC_MOVE);
		}
		*/
		
	}
	
	
	public Document getXml(URL url) throws Exception {
		
		URLConnection urlConnection = url.openConnection();
		urlConnection.addRequestProperty("Accept", "application/xml");
		
		Document doc;
		
		try( InputStream is = urlConnection.getInputStream() ) {
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(is);
			
		} catch (Exception e) {
			throw(e);
		}
		
		return doc;
		
	}
	
	
	public Optional<ResumptionToken> getResumptionToken(Document doc) {
		
		NodeList nl = doc.getElementsByTagName("resumptionToken");
		
		if (nl.getLength() > 0) {
			
			Node node = nl.item(0);
			
			ResumptionToken rt = new ResumptionToken(
				node.getTextContent(),
				node.getAttributes().getNamedItem("completeListSize").getTextContent(),
				node.getAttributes().getNamedItem("cursor").getTextContent()
			);
			
			return Optional.of(rt);
		}
		else return Optional.empty();
	}
	

}


class ResumptionToken {
	
	public final String token;
	public final Integer listSize;
	public final Integer cursor;
	
	public ResumptionToken(String token, String listSize, String cursor) {
		this.token = token;
		this.listSize = parseIntOrNull(listSize);
		this.cursor = parseIntOrNull(cursor);
	}

	@Override
	public String toString() {
		return "ResumptionToken [token=" + token + ", listSize=" + listSize + ", cursor=" + cursor + "]";
	}
	
	static Integer parseIntOrNull(String s) {
		
		try {
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e) {
			return null;
		}
		
	}
	
}

