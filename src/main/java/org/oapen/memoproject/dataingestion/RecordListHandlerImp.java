package org.oapen.memoproject.dataingestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.oapen.memoproject.dataingestion.harvest.RecordListHandler;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

public final class RecordListHandlerImp implements RecordListHandler {
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	
	@Autowired
	PersistenceService perservice;
	
	@Override
	public void process(List<Element> elements) {
		
		List<Title> titles = new ArrayList<>();
		
		System.out.println("CLASSSSS " + perservice);
		
		elements.forEach(el -> { 
			
			EntitiesSource m = new XOAIDocumentParser(el);
			
			Optional<Title> title = m.getTitle();
			
			title.ifPresent(t -> {

				if (t.isDeleted()) {
					// System.out.println(">>>>>>> DELETE " + t.getHandle());
					
					// TODO handle deletions 
				}
				else if (t.isComplete()) {
					
					titles.add(t);
					
					m.getPublisher().ifPresent(perservice::savePublisher);
					
					perservice.saveClassifications(m.getClassifications());
					perservice.saveContributors(m.getContributors());
					try { perservice.saveFunders(m.getFunders());} catch (RuntimeException e) { /* log da shit with t handle */}	
					perservice.saveTitle(t);
					
					// t.getFunders().stream().forEach(System.out::println);
				}
			});
			
		});
		

		System.out.println("TOTAL: " + titles.size());
	}
	
	
	
}
