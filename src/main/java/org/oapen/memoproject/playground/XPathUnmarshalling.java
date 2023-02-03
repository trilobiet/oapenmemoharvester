package org.oapen.memoproject.playground;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.persistence.exceptions.JAXBException;
import org.oapen.memoproject.playground.entities.Funder;
import org.oapen.memoproject.playground.entities.Publisher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XPathUnmarshalling {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, JAXBException, javax.xml.bind.JAXBException {
		
		String xmlFile = "./src/main/resources/xoai-response.xml";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xml = db.parse(xmlFile);
		
		NodeList s = xml.getElementsByTagName("record");
		
		// create a new XML Document for first record 
		Node record = s.item(17);
		
		Document newXmlDocument = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();
        Element root = newXmlDocument.createElement("root");
        newXmlDocument.appendChild(root);
        
        Node copyNode = newXmlDocument.importNode(record, true);
        root.appendChild(copyNode);
        
		//Get XPath 
		XPath xpath = XPathFactory.newInstance().newXPath();

		//Get author match
		//Node authornode = (Node) xpath.evaluate("//element[@name='author']/element", root, XPathConstants.NODE);

		//Get funder match
		NodeList publishernodes = (NodeList) xpath.evaluate("//element[@name='oapen.relation.isPublishedBy']", root, XPathConstants.NODESET);
		NodeList fundernodes = (NodeList) xpath.evaluate("//element[@name='oapen.relation.isFundedBy']", root, XPathConstants.NODESET);
		
		
		for (int i=0; i<publishernodes.getLength(); i++) {
			System.out.println(nodeToString(publishernodes.item(i)));
		}	

		for (int i=0; i<fundernodes.getLength(); i++) {
			System.out.println(nodeToString(fundernodes.item(i)));
		}	

		
		System.out.println("publishers: " + publishernodes.getLength());
		System.out.println("funders: " + fundernodes.getLength());
        
        // ----------------

		JAXBContext funderContext = JAXBContext.newInstance(Funder.class);
		JAXBContext publisherContext = JAXBContext.newInstance(Publisher.class);
		//JAXBContext jc = JAXBContext.newInstance(Publisher.class,Funder.class); 
		// cannot do this ^ they will clash on the same @XmlRootElement(name = "element") 

        // IMPORTANT: Is the correct implementation active?
		// make sure jaxb.properties is in the same package as the entities!
        // should be org.eclipse.persistence.jaxb.JAXBContext
        // https://stackoverflow.com/questions/63468873/moxy-xpath-unmarshalling-element-is-null
        System.out.println(funderContext.getClass().getName()); 

        Unmarshaller fUnmarshaller = funderContext.createUnmarshaller();
        Unmarshaller pUnmarshaller = publisherContext.createUnmarshaller();
        
        
        //jc = JAXBContext.newInstance(Funder.class);
        //unmarshaller = jc.createUnmarshaller();
        
        for (int i=0; i<publishernodes.getLength(); i++) {
        	
        	Node publishernode = publishernodes.item(i);
        
	        if (publishernode != null) {
	        	Publisher publisher = (Publisher) pUnmarshaller.unmarshal(publishernode);
	        	System.out.println(publisher);
	        }
        }

        for (int i=0; i<fundernodes.getLength(); i++) {
        	
        	Node fundernode = fundernodes.item(i);
        	
        	//System.out.println(nodeToString(fundernode));
        	
        	// THIS IS HOW WE MUST DO IT - skip the XmlPath bindings
        	
        	Node n = (Node) xpath.evaluate("//field[@name='handle']/text()",fundernode, XPathConstants.NODE);
        	
        	System.out.println(nodeToString(n));
        	
        	/*
	        if (fundernode != null) {
	        	Funder funder = (Funder) fUnmarshaller.unmarshal(fundernode);
	        	System.out.println(funder);
	        }*/
        }
        
        
	}
	
	
	private static String nodeToString(Node node) {
	    StringWriter sw = new StringWriter();
	    try {
	        Transformer t = TransformerFactory.newInstance().newTransformer();
	        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        t.setOutputProperty(OutputKeys.INDENT, "yes");
	        t.transform(new DOMSource(node), new StreamResult(sw));
	    } catch (TransformerException te) {
	        System.out.println("nodeToString Transformer Exception");
	    }
	    return sw.toString();
	}
	
	

}




