package org.oapen.memoproject.dataingestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.w3c.dom.Element;

public final class RecordListHandlerImp implements RecordListHandler {
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	
	//TODO handle deletions
	
	@Override
	public void process(List<Element> elements) {
		
		List<Title> titles = new ArrayList<>();
		
		elements.forEach(el -> { 
			
			ElementToEntitiesMapper m = new XpathElementToEntitiesMapper(el);
			
			Optional<Title> title = m.getItem();
			
			title.ifPresent(t -> {

				if (t.isDeleted()) {
					System.out.println(">>>>>>> DELETE " + t.getHandle());
				}
				else if (t.isComplete()) {
					System.out.println(t);
					titles.add(t);
				}
			});
			
		});
		

		System.out.println("TOTAL: " + titles.size());
	}
	
	
	
}
