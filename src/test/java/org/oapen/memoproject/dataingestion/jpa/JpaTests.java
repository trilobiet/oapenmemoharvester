package org.oapen.memoproject.dataingestion.jpa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.GrantData;
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
	ContributorRepository contributorRepository;
	
	@Autowired
	PublisherRepository publisherRepository;

	@Autowired
	FunderRepository funderRepository;
	
	@Autowired
	ClassificationRepository classificationRepository;

	
	@BeforeEach
	public void setUp() {
		
	}

	//@Test
	//@Order(100)
	public void tearDown() {
		
		clearAllData();

		List<Title> titles = titleRepository.findAll();
		List<Contributor> contributors = contributorRepository.findAll();
		
		assertTrue(titles.isEmpty() && contributors.isEmpty());
	}

	
	private void clearAllData() {
		
		titleRepository.deleteAll();
		contributorRepository.deleteAll();
		funderRepository.deleteAll();
		publisherRepository.deleteAll();
		classificationRepository.deleteAll();
	}
	
	@Test @Order(1)
	public void delete() {
		
		clearAllData();
		
		List<Title> titles = titleRepository.findAll();
		List<Contributor> contributors = contributorRepository.findAll();
		
		assertTrue(titles.isEmpty());
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
		
		Title title = new Title("hndl1");
		title.setTitle("Whatever");
		
		Title tSaved = titleRepository.save(title);
		assertFalse(tSaved.getHandle().equals("bogus"));
	}
	
	@Test @Order(4)
	public void should_save_languages() {
		
		Title title1 = new Title("hndl2");
		title1.setTitle("Whatever again");
		title1.setLanguages(new HashSet<>(Arrays.asList("FR","EN","NL")));
		Title tSaved = titleRepository.save(title1);
		assertTrue(tSaved.getLanguages().size()==3);
		assertTrue(tSaved.getLanguages().contains("FR"));
	}

	
	@Test @Order(5)
	public void should_save_identifiers() {
		
		Title title1 = new Title("hndl3");
		title1.setTitle("You name it");
		
		Set<Identifier> identifiers = new HashSet<>();

		Identifier id1 = new Identifier();
		id1.setId("123333");
		id1.setType("ONIXhoor");
		identifiers.add(id1);
		
		Identifier id2 = new Identifier();
		id2.setId("432424242");
		id2.setType("DOI");
		identifiers.add(id2);

		title1.setIdentifiers(identifiers);

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
		
		Title title1 = new Title("hndl4");
		title1.setTitle("Whatever");
		title1.setCollection("falderalderee");
		Set<ExportChunk> chunks =  new HashSet<>();
    	
    	chunks.add( new ExportChunk("marcxml", "HALLObullublublubblublub") );
    	chunks.add( new ExportChunk("kbart", "van HALLO je hup falderiedee") );
    	title1.setExportChunks(chunks);
    	
    	Title t1saved = titleRepository.save(title1);
    	
		Title title2 = new Title("hndl5");
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
		
		Title title1 = new Title("hndl6");
		title1.setTitle("Whatever");
    	
		Publisher pub1 = new Publisher();
		pub1.setHandle("p12345");
		pub1.setName("Pietje");
		pub1.setWebsite("www.acme.com");
		title1.setPublisher(pub1);
		
		Title title2 = new Title("hndl7");
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
	public void should_save_funder() {
		
		Title title1 = new Title("hndl8");
		title1.setTitle("TESTJE8");
		
		// removes doubles
		Set<String> acronyms = new HashSet<>(Arrays.asList("acr3","acr1","acr2","acr2"));
		
		Funder funder = new Funder();
		funder.setHandle("funder1");
		funder.setName("ERCEEEE");
		funder.setNumber("1234567");
		funder.setAcronymSet(acronyms);
		
		funderRepository.save(funder);
		
		Set<Funder> funders = new HashSet<>();
		funders.add(funder);
		title1.setFunders(funders);
		
		Title t1saved = titleRepository.save(title1);
		Optional<Funder> f2 = t1saved.getFunders().stream().filter(f -> f.getHandle().equals("funder1")).findFirst(); 
		
		System.out.println(f2.get().getAcronyms());
		
		assertTrue(f2.isPresent() 
				&& f2.get().getAcronyms().contains("acr3"));	
		assertTrue(t1saved.getFunders().size() == 1);
	}	
	

	@Test @Order(9)
	public void should_save_subject_other() {
		
		Title title1 = new Title("hndl9");
		title1.setSubjectsOther(new HashSet<>(Arrays.asList("subject1","subject2","subject3")));
		
		Title t1saved = titleRepository.save(title1);
		
		assertTrue(t1saved.getSubjectsOther().size() == 3);
	}	
	
	
	@Test @Order(10)
	public void should_save_classifications() {
		
		Title title1 = new Title("hndl10");
		
		Set<Classification> classifications = new HashSet<>();
		
		Classification c = new Classification();
		c.setCode("AABC");
		c.setDescription("bogus");
		classifications.add(c);
		classifications.add(null); // TODO

		Classification c2 = new Classification();
		c2.setCode("XXYQ");
		c2.setDescription("more bogus");
		classifications.add(c2);
		title1.setClassifications(classifications);

		Classification c3 = new Classification();
		c3.setCode("ZXYQZ");
		c3.setDescription("even more bogus");
		title1.addClassification(c3);
		
		Title t1saved = titleRepository.save(title1);
		assertTrue(t1saved.getClassifications().size()==3);
		assertTrue(t1saved.getClassifications().contains(c3));
		
	}

	
	@Test @Order(11)
	public void should_save_contribution() {
		
		Title title1 = new Title("hndl12");
		
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
	
	
	@Test @Order(12)
	public void should_save_grant_data() {
		
		Title title1 = new Title("hndl13");
		
		GrantData grantData1 = new GrantData("number","grant123");
		GrantData grantData2 = new GrantData("project","project456");
		
		title1.addGrantData(grantData1);
		title1.addGrantData(grantData2);
		
		Title t1saved = titleRepository.save(title1);
		
		assertTrue(t1saved.getGrantData().size() == 2);
	}	
	
	
}
