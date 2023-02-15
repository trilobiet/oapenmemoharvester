package org.oapen.memoproject.dataingestion.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.Funding;
import org.oapen.memoproject.dataingestion.jpa.entities.Identifier;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestPropertySource(locations="/application.properties")
@TestMethodOrder(OrderAnnotation.class)
public class JpaTests {
	
	@Autowired
	TitleRepository titleRepository;

	@Autowired
	FunderRepository funderRepository;

	@Autowired
	ContributorRepository contributorRepository;
	
	
	@Test @Order(1)
	public void delete() {
		
		titleRepository.deleteAll();
		funderRepository.deleteAll();
		contributorRepository.deleteAll();
		
		List<Title> titles = titleRepository.findAll();
		List<Funder> funders = funderRepository.findAll();
		List<Contributor> contributors = contributorRepository.findAll();
		
		assertTrue(titles.isEmpty());
		assertTrue(funders.isEmpty());
		assertTrue(contributors.isEmpty());
	}
	
	@Test @Order(2)
	public void should_find_no_titles_if_repository_is_empty() {
		titleRepository.deleteAll();
		List<Title> titles = titleRepository.findAll();
		assertTrue(titles.isEmpty());
	}
	
	@Test @Order(3)
	public void should_save_a_title() {
		
		Title title = new Title();
		title.setId("0001");
		title.setHandle("hndl1");
		title.setTitle("Whatever");
		
		Title tSaved = titleRepository.save(title);
		assertTrue(tSaved.getId().equals("0001"));
		assertFalse(tSaved.getId().equals("bogus"));
	}
	
	@Test @Order(4)
	public void should_save_languages() {
		
		Title title1 = new Title();
		title1.setId("0002");
		title1.setHandle("hndl2");
		title1.setTitle("Whatever again");
		title1.setLanguages(new HashSet<>(Arrays.asList("FR","EN","NL")));
		Title tSaved = titleRepository.save(title1);
		assertTrue(tSaved.getLanguages().size()==3);
		assertTrue(tSaved.getLanguages().contains("FR"));
	}

	
	@Test @Order(5)
	public void should_save_identifiers() {
		
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
		
		assertTrue(tSaved.getIdentifiers().size()==3);
		assertTrue(tSaved.getIdentifiers().contains(id2));
	}
	
	
	@Test @Order(6)
	public void should_save_export_chunks() {
		
		Title title1 = new Title();
		title1.setId("0004");
		title1.setHandle("hndl4");
		title1.setTitle("Whatever");
		title1.setCollection("falderalderee");
    	
    	title1.addExportChunk( new ExportChunk("marcxml", "HALLObullublublubblublub") );
    	title1.addExportChunk( new ExportChunk("kbart", "van HALLO je hup falderiedee") );
    	
    	Title t1saved = titleRepository.save(title1);
    	
		Title title2 = new Title();
		title2.setId("0005");
		title2.setHandle("hndl5");
		title2.setCollection("6559559");
		title2.setTitle("Bonkers");

		title2.addExportChunk( new ExportChunk("onix", "HALLOuh23huhjhewuh") );
		title2.addExportChunk( new ExportChunk("ris", "rrrrrrHALLOrrrrrrrrrr") );
		title2.addExportChunk( new ExportChunk("marcxml", "xmxlxmxHALLOxxlx") );
		
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
    	
		Publisher pub1 = new Publisher();
		pub1.setHandle("p12345");
		pub1.setName("Pietje");
		pub1.setWebsite("www.acme.com");
		title1.setPublisher(pub1);
		
		Title title2 = new Title();
		title2.setId("0007");
		title2.setHandle("hndl7");
		title2.setCollection("6559559");
		title2.setTitle("Bonkers");

		Publisher pub2 = new Publisher();
		pub2.setHandle("p12345");
		pub2.setName("Pietje");
		pub2.setWebsite("www.burp.com");
		title2.setPublisher(pub2);
		
		Title t1saved = titleRepository.save(title1);
		Title t2saved = titleRepository.save(title2);
		
		assertTrue(t1saved.getPublisher().equals(t2saved.getPublisher()));
	}
	
	
	@Test @Order(8)
	public void should_save_funding() {
		
		Title title1 = new Title();
		title1.setId("0008");
		title1.setHandle("hndl8");
		title1.setTitle("TESTJE8");
		
		Funder funder = new Funder();
		funder.setHandle("funder1");
		funder.setName("ERCEEEE");
		
		funderRepository.save(funder);
		
		Funding f = Funding.builder()
			.handleFunder(funder.getHandle())
			.grantNumber("grant12345")
			.build();
		
		title1.addFunding(f);
		
		Title t1saved = titleRepository.save(title1);

		assertTrue(t1saved.getFundings().size() == 1);
	}	
	

	@Test @Order(9)
	public void should_save_subject_other() {
		
		Title title1 = new Title();
		title1.setId("0009");
		title1.setHandle("hndl9");
		title1.setSubjectsOther(new HashSet<>(Arrays.asList("subject1","subject2","subject3")));
		
		Title t1saved = titleRepository.save(title1);
		
		assertTrue(t1saved.getSubjectsOther().size() == 3);
	}	
	
	
	@Test @Order(10)
	public void should_save_classifications() {
		
		Title title1 = new Title();
		title1.setId("0010");
		title1.setHandle("hndl10");
		
		Classification c = new Classification();
		c.setCode("AABC");
		c.setDescription("bogus");
		title1.addClassification(c);

		Classification c2 = new Classification();
		c2.setCode("XXYQ");
		c2.setDescription("more bogus");
		title1.addClassification(c2);

		Classification c3 = new Classification();
		c3.setCode("ZXYQZ");
		c3.setDescription("even more bogus");
		title1.addClassification(c3);
		
		Title t1saved = titleRepository.save(title1);
		assertTrue(t1saved.getClassifications().size()==3);
		assertTrue(t1saved.getClassifications().contains(c3));
		
	}

	
	@Test @Order(11)
	public void should_save_dates() {
		
		Title title1 = new Title();
		title1.setId("0011");
		title1.setHandle("hndl11");
		
		title1.setDatesAccessioned(new HashSet<>(Arrays.asList("2023-01-01","2023-01-02","2023-01-03")));
		
		Title t1saved = titleRepository.save(title1);
		assertTrue(t1saved.getDatesAccessioned().size()==3);
		assertTrue(t1saved.getDatesAccessioned().contains("2023-01-02"));
	}

	
	@Test @Order(12)
	public void should_save_contribution() {
		
		Title title1 = new Title();
		title1.setId("0012");
		title1.setHandle("hndl12");
		
		Contributor cor = new Contributor();
		cor.setName("Pipo de Clown");
		cor.setOrcid("1234567890");
		
		contributorRepository.save(cor);
				
		Contribution c = new Contribution( cor.getName(), "AUTHOR");
		title1.addContribution(c);

		Title t1saved = titleRepository.save(title1);
		assertTrue(t1saved.getContributions().size()==1);
		assertTrue(t1saved.getContributions().contains(c));

	}	
}
