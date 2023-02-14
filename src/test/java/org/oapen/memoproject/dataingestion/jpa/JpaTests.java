package org.oapen.memoproject.dataingestion.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.Funding;
import org.oapen.memoproject.dataingestion.jpa.entities.Identifier;
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
@TestMethodOrder(OrderAnnotation.class)
public class JpaTests {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	TitleRepository titleRepository;

	@Autowired
	FunderRepository funderRepository;

	
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	
	@Test @Order(1)
	public void delete() {
		
		titleRepository.deleteAll();
	}

	
	
	//@Test @Order(2)
	public void should_find_no_titles_if_repository_is_empty() {
		titleRepository.deleteAll();
		List<Title> titles = titleRepository.findAll();
		assertTrue(titles.isEmpty());
	}
	
	//@Test @Order(3)
	public void should_save_a_title() {
		
		Title title = new Title();
		title.setId("0001");
		title.setHandle("hndl1");
		title.setTitle("Whatever");
		
		Title tSaved = titleRepository.save(title);
		assertTrue(tSaved.getId().equals("1234567"));
		assertFalse(tSaved.getId().equals("0234567"));
	}
	
	//@Test @Order(4)
	public void set_language() {
		
		Title title1 = new Title();
		title1.setId("0002");
		title1.setHandle("hndl2");
		title1.setTitle("Whatever again");
		title1.setLanguages(new HashSet<>(Arrays.asList("FR","EN","NL")));
		Title tSaved = titleRepository.save(title1);
		assertTrue(tSaved.getLanguages().size()==3);
		assertTrue(tSaved.getLanguages().contains("FR"));
	}

	
	//@Test @Order(5)
	public void set_identifiers() {
		
		Title title1 = new Title();
		title1.setId("0003");
		title1.setHandle("hndl3");
		title1.setTitle("You name it");

		Identifier id1 = new Identifier();
		id1.setId("123333");
		id1.setType("ONIXhoor");
		title1.addIdentifier(id1);
		
		Identifier id2 = new Identifier();
		id2.setId("432424242");
		id2.setType("DOI");
		title1.addIdentifier(id2);
		
		Identifier id3 = new Identifier();
		id3.setId("mammelou");
		id3.setType("ISBN");
		title1.addIdentifier(id3);
		
		Title tSaved = titleRepository.save(title1);
		
		//Title tSaved = transactionTemplate.execute((conn) -> {
	    //    return entityManager.persist(title1);
	    //});
		
		assertTrue(tSaved.getIdentifiers().size()==3);
		assertTrue(tSaved.getIdentifiers().contains(id2));
	}
	
	
	@Test @Order(6)
	public void should_save_export_chunk() {
		
		Title title1 = new Title();
		title1.setId("0004");
		title1.setHandle("hndl4");
		title1.setTitle("Whatever");
		title1.setCollection("falderalderee");
    	
    	title1.addExportChunk("marcxml", "HALLObullublublubblublub");
    	title1.addExportChunk("kbart", "van HALLO je hup falderiedee");
    	
    	Title t1saved = titleRepository.save(title1);
    	
		Title title2 = new Title();
		title2.setId("0005");
		title2.setHandle("hndl5");
		title2.setCollection("6559559");
		title2.setTitle("Bonkers");

		title2.addExportChunk("onix", "HALLOuh23huhjhewuh");
		title2.addExportChunk("ris", "rrrrrrHALLOrrrrrrrrrr");
		title2.addExportChunk("marcxml", "xmxlxmxHALLOxxlx");
		
		Title t2saved = titleRepository.save(title2);
		
		assertTrue(t1saved.getExportChunks().size()==2);
		assertTrue(t2saved.getExportChunks().size()==3);

	}


	@Test @Order(7)
	public void should_share_a_publisher() {
		
		Title title1 = new Title();
		title1.setId("0006");
		title1.setHandle("hndl6");
		title1.setTitle("Whatever");
		title1.setCollection("falderalderee");
		
		Set<String> dates = new HashSet<String>();
		dates.add("2023-02-08");
		dates.add("2023-02-07");
		dates.add("2023-02-06");
		dates.add("2023-02-07");
		dates.add("2023-02-08");
		title1.setDatesAccessioned(dates);
    	
		Publisher pub1 = new Publisher();
		pub1.setId("p12345");
		pub1.setName("Pietje");
		pub1.setWebsite("www.acme.com");
		title1.setPublisher(pub1);
		
    	title1.addExportChunk("marcxml", "HAL666LObullublublubblublub");
    	title1.addExportChunk("kbart", "van HALLO je hup falderiedee");

		Title title2 = new Title();
		title2.setId("0007");
		title2.setHandle("hndl7");
		title2.setCollection("6559559");
		title2.setTitle("Bonkers");
		
		title2.addExportChunk("onix", "HALLOuh23huhjhewuh");
		title2.addExportChunk("ris", "rrrrrrHALLOrrrrrrrrrr");
		title2.addExportChunk("marcxml", "xmxlxmxHALLOxxlx");
		

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

		Classification c3 = new Classification();
		c3.setCode("ZXYQZ");
		c3.setDescription("even more bogus");
		title2.addClassification(c3);
		
		Title t1saved = titleRepository.save(title1);
		Title t2saved = titleRepository.save(title2);
		
		assertTrue(t1saved.getPublisher().equals(t2saved.getPublisher()));

	}

	
	
	
	@Test @Order(8)
	public void funding_insert() {
		
		Title title1 = new Title();
		title1.setId("0008");
		title1.setHandle("hndl8");
		title1.setTitle("TESTJE8");
		
		Funder funder = new Funder();
		funder.setId("funder1");
		funder.setName("ERCEEEE");
		
		funderRepository.save(funder);
		
		Funding f = new Funding();
		f.setHandleFunder("funder1");
		f.setGrantNumber("grant12345");
		
/*		Funder fun = new Funder();
		fun.setId("42134141423");
		fun.setName("ERCee");
		fun.setAcronyms("blabla"); */
		
		title1.addFunding(f);
		
		Title t1saved = titleRepository.save(title1);

		assertTrue(t1saved.getFundings().size() == 1);
		
		System.out.println("FFFFFFFFFFFFFFFFFFFFFFFUNDERRRRRRRRRRRRRRRRRRRRR");
	}	
	

	//@Test @Order(9)
	public void subject_other_insert() {
		
		Title title1 = new Title();
		title1.setId("0009");
		title1.setHandle("hndl9");
		title1.setSubjectsOther(new HashSet<>(Arrays.asList("bloemen","vliegtuigen","architectuur")));
		
		Title t1saved = titleRepository.save(title1);
	}	
	
	
	//@Test @Order(10)
	public void burp() {
		
		Title title1 = new Title();
		title1.setId("0010");
		title1.setHandle("hndl10");
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
		
		
		Optional<Title> q = titleRepository.findById("1234567");
		
		System.out.println("SIZE = " + q.get().getDatesAccessioned().size());
		
		assertTrue(t1saved.getDatesAccessioned().size()==3);
		
	}

}
