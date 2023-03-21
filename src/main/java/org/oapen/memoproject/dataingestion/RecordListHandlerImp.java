package org.oapen.memoproject.dataingestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.oapen.memoproject.dataingestion.harvest.RecordListHandler;
import org.oapen.memoproject.dataingestion.jpa.JpaPersistenceService;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

public final class RecordListHandlerImp implements RecordListHandler {
	
	XPath xpath = XPathFactory.newInstance().newXPath();
	
	@Autowired
	PersistenceService perservice;
	
	private static final Logger logger = 
		LoggerFactory.getLogger(JpaPersistenceService.class);
	
	
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
					
					try { m.getPublisher().ifPresent(perservice::savePublisher);} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("ERROR occurred at title with handle " + t.getHandle());
					}
					
					try { perservice.saveClassifications(m.getClassifications());} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("ERROR occurred at title with handle " + t.getHandle());
					}
					
					try { perservice.saveContributors(m.getContributors());} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("ERROR occurred at title with handle " + t.getHandle());
						logger.error(t.getContributions().toString());
					}
					
					try { perservice.saveFunders(m.getFunders());} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("ERROR occurred at title with handle " + t.getHandle());
						logger.error(t.getFunders().toString());
					}	
					
					try { perservice.saveTitle(t);} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("ERROR occurred at title with handle " + t.getHandle());
						logger.error(t.getContributions().toString());
					}
					
					// t.getFunders().stream().forEach(System.out::println);
				}
			});
			
		});
		

		System.out.println("TOTAL: " + titles.size());
	}
	
	
	
}
