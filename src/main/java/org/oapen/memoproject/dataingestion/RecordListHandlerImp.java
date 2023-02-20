package org.oapen.memoproject.dataingestion;

import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;

public final class RecordListHandlerImp implements RecordListHandler {
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	
	@Override
	public void process(List<Element> elements) {
		
		
		elements.forEach(el -> { 
			
			System.out.println(el.getChildNodes().item(1).getChildNodes().item(1).getTextContent());
		
			try {
				String handle = (String) xpath.evaluate("//element[@name='others']/field[@name='handle']", el, XPathConstants.STRING);
				System.out.println(handle);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
		});
		
		
	}

	
	
}
