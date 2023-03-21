package org.oapen.memoproject.dataingestion.metadata;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

class XMLStringConverter {

	//Parser that produces DOM object trees from XML content
	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	protected Document stringToXMLDocument(String xmlString) {

		//API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			//Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();
			//Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	protected String nodeToString(final Node node) {

		final StringWriter writer = new StringWriter();
	
		try {
			
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // indent to show results in a more human readable format
			transformer.transform(new DOMSource(node), new StreamResult(writer));
			
		} catch(final TransformerException e) {
			e.printStackTrace();
		}
		
		return writer.toString();
	}
	

}
