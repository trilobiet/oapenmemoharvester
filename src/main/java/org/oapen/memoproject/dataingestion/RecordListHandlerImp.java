package org.oapen.memoproject.dataingestion;

import java.util.List;
import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.w3c.dom.Element;

public final class RecordListHandlerImp implements RecordListHandler {
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	
	@Override
	public void process(List<Element> elements) {
		
		
		elements.forEach(el -> { 
			
			//System.out.println(el.getChildNodes().item(1).getChildNodes().item(1).getTextContent());
			
			
			ElementToEntitiesMapper m = new XpathElementToEntitiesMapper(el);
			
			//Title title = new Title();
			try {
				Optional<String> handle = m.getHandle();
				Optional<String> sysId = m.getSysId();
				
				//title.setHandle(m.getHandle().get());
			} catch (MappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
				
		});
		
		
	}
	
	
	public boolean isValidRecord(Element element) {
		
		return true;
		
		
	}

	
	
}
