package org.oapen.memoproject.dataingestion.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;


@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestPropertySource(locations="/application.properties")
public class JpaTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	TitleRepository repository;
	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	
	//@Test
	public void delete() {
		
		repository.deleteAll();
	}

	
	
	public void doeIets() {
		
	}
	
	
	
	//@Test
	public void should_find_no_titles_if_repository_is_empty() {
		repository.deleteAll();
		List<Title> titles = repository.findAll();
		assertTrue(titles.isEmpty());
	}
	
	//@Test
	public void should_save_a_title() {
		
		Title title = new Title();
		title.setId("1234567");
		title.setHandle("hndl1");
		title.setTitle("Whatever");
		
		Title tSaved = repository.save(title);
		assertTrue(tSaved.getId().equals("1234567"));
		assertFalse(tSaved.getId().equals("0234567"));
	}
	
	@Test
	public void should_share_a_publisher() {
		
		Title title1 = new Title();
		title1.setId("1234567");
		title1.setHandle("hndl1");
		title1.setTitle("Whatever");
		title1.setCollection("falderalderee");
		Set<String> dates = new HashSet<String>();
		dates.add("2023-02-08");
		dates.add("2023-02-07");
		dates.add("2023-02-06");
		dates.add("2023-02-07");
		dates.add("2023-02-08");
		title1.setDatesAccessioned(dates);
		
		/*
    	Set<ExportChunk> chunks = new HashSet<>();

    	ExportChunk c = new ExportChunk();
    	ExportChunkId id = new ExportChunkId();
    	id.setTitle(title1);
    	id.setType("marciemarc");
    	c.setId(id);
    	c.setContent("xml xml22 xmyyyl xml");
    	chunks.add(c);

    	ExportChunk c2 = new ExportChunk();
    	ExportChunkId id2 = new ExportChunkId();
    	id2.setTitle(title1);
    	id2.setType("onix");
    	c2.setId(id2);
    	c2.setContent("NIX NIX");
    	chunks.add(c2);
    	title1.setExportChunks(chunks);
    	
    	*/
    	
    	title1.addExportChunk("marcxml", "bullublublubblublub");
    	title1.addExportChunk("kbart", "van je hup falderiedee");
    	
		Publisher pub1 = new Publisher();
		pub1.setId("p12345");
		pub1.setName("Pietje");
		pub1.setWebsite("www.acme.com");
		title1.setPublisher(pub1);

		Title title2 = new Title();
		title2.setId("1234568");
		title2.setHandle("hndl2");
		title2.setCollection("6559559");
		title2.setTitle("Bonkers");

		Publisher pub2 = new Publisher();
		pub2.setId("p12345");
		pub2.setName("Pietje");
		pub2.setWebsite("www.burp.com");
		title2.setPublisher(pub2);
		
		title2.addExportChunk("onix", "uh23huhjhewuh");
		title2.addExportChunk("ris", "rrrrrrrrrrrrrrrr");
		title2.addExportChunk("marcxml", "xmxlxmxxxlx");
		
		Classification c = new Classification();
		c.setCode("AABC");
		c.setDescription("bogus");
		title2.addClassification(c);

		Classification c2 = new Classification();
		c2.setCode("XXYQ");
		c2.setDescription("more bogus");
		title2.addClassification(c2);

		
		Title t1saved = repository.save(title1);
		Title t2saved = repository.save(title2);
		
		assertTrue(t1saved.getPublisher().equals(t2saved.getPublisher()));

	}
	
	//@Test
	public void burp() {
		
		Title title1 = new Title();
		title1.setId("1234567");
		title1.setHandle("hndl1");
		Set<String> dates = new HashSet<String>();
		dates.add("2023-02-08");
		dates.add("2023-02-07");
		dates.add("2023-02-06");
		dates.add("2023-02-07");
		dates.add("2023-02-08");
		title1.setDatesAccessioned(dates);
		
		// Title t1saved = repository.save(title1);
		
		Title t1saved = transactionTemplate.execute((conn) -> {
	        return entityManager.persist(title1);
	    });
		
		
		Optional<Title> q = repository.findById("1234567");
		
		System.out.println("SIZE = " + q.get().getDatesAccessioned().size());
		
		assertTrue(t1saved.getDatesAccessioned().size()==3);
		
	}

}
