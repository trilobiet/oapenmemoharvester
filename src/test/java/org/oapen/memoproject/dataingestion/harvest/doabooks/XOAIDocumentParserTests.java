package org.oapen.memoproject.dataingestion.harvest.doabooks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.PeerReview;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XOAIDocumentParserTests {
	
	private final String xmlrecord1 = TestConstants.xmlrecord1;
	private DoabooksXOAIDocumentParser source1;
	
	/*
	 * Tests here only where DOAB XOAI differs from OAPEB XOAI
	 */
	
	@BeforeEach
    void setUp() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc1 = db.parse(new InputSource( new StringReader( xmlrecord1 ) ));
		Element el1 = (Element) doc1.getElementsByTagName("record").item(0);
		
		source1 = new DoabooksXOAIDocumentParser(el1);
	}	

	
	@Test
	public void should_find_downloadUrls() {

		Set<String> expectedDownloadUrls = new HashSet<>(
			Arrays.asList(
				"https://www.ksp.kit.edu/9783866447004"
			)	
		);
		Set<String> foundDownloadUrls = source1.getTitle().get().getDownloadUrl();
		
		assertTrue(foundDownloadUrls.containsAll(expectedDownloadUrls));
	}
	
	
	@Test
	public void should_find_peerreview() {
		
		ArrayList<PeerReview> foundPeerReviews = new ArrayList<PeerReview>(source1.getPeerReviews());
		// System.out.println("PEERREVIEWS = " + foundPeerReviews);
		
		assertTrue(foundPeerReviews.size() > 0); 
		assertEquals("8ad5c235-9810-49eb-b358-27c8675324d9", foundPeerReviews.getFirst().getId());
		assertEquals(2, foundPeerReviews.getFirst().getReviewerTypes().size());
	}

}


