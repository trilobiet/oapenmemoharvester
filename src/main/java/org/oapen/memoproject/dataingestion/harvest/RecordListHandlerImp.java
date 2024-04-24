package org.oapen.memoproject.dataingestion.harvest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.oapen.memoproject.dataingestion.jpa.PersistenceService;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Implementation of RecordListHandler that parses each element 
 * as an XOAI Record Document, extracts JPA Entities and sends them to
 * a persistence service.   
 * 
 * @author acdhirr
 *
 */
public final class RecordListHandlerImp implements RecordListHandler {
	
	private final PersistenceService perservice;
	
	public RecordListHandlerImp(PersistenceService perservice) {
		this.perservice = perservice;
	}

	private static final Logger logger = 
		LoggerFactory.getLogger(RecordListHandlerImp.class);
	
	
	/**
	 * Process each Element as an XOAI Document and send the resulting JPA
	 * Entities to a persistence layer
	 */
	@Override
	public List<String> process(List<Element> elements) {
		
		List<String> insertedHandles = new ArrayList<>();
		
		elements.forEach(el -> { 
			
			EntitiesSource m = new XOAIDocumentParser(el);
			
			Optional<Title> title = m.getTitle();
			
			title.ifPresent(t -> {

				if (t.isDeleted()) {
					
					try { perservice.deleteTitle(t);} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("Could not delete title with handle " + t.getHandle());
					}
				}
				else if (t.isComplete()) {
					
					try { m.getPublisher().ifPresent(perservice::savePublisher);} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("savePublisher at title with handle " + t.getHandle());
					}
					
					try { perservice.saveClassifications(m.getClassifications());} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("saveClassifications at title with handle " + t.getHandle());
					}
					
					try { perservice.saveContributors(m.getContributors());} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("saveContributors at title with handle " + t.getHandle());
						logger.error(t.getContributions().toString());
					}
					
					try { perservice.saveFunders(m.getFunders());} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("saveFunders at title with handle " + t.getHandle());
						logger.error(t.getFunders().toString());
					}	
					
					try { perservice.saveTitle(t);} catch (RuntimeException e) { 
						/* log da shit with t handle */
						logger.error("saveTitle with handle " + t.getHandle());
						logger.error(t.getTitle().toString());
					}
					
					insertedHandles.add(t.getHandle());
					
				}
			});
			
		});

		return insertedHandles;
	}
	
	
	
}
