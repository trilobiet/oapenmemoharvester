package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.Funding;
import org.oapen.memoproject.dataingestion.jpa.entities.Identifier;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ElementToEntitiesMapperTests {
	
	ElementToEntitiesMapper mapper;
	
	@BeforeEach
    void setUp() throws Exception {

		String s = "./src/test/resources/xoai-response-short.xml";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xml = db.parse(s);
		
		ListRecordsDocument lrdoc = new ListRecordsDocumentImp(xml);
		
		Element record = lrdoc.getRecords().get(1);
		
		this.mapper = new XpathElementToEntitiesMapper(record);
	}	
	

	@Test
	public void should_find_handler() throws MappingException {

		assertEquals("20.500.12657/60840", mapper.getHandle().get());
	}
	
	@Test
	public void should_find_sysId() throws MappingException {

		assertEquals("22222222-3aff-4e7b-9a1c-ce8c75fa9530", mapper.getSysId().get());
	}


	@Test
	public void should_find_publisher() throws MappingException {
		
		Publisher expectedPublisher = new Publisher("20.500.12657/22488","Springer Nature");
		Publisher foundPublisher = mapper.getPublisher().get();
		
		assertEquals(expectedPublisher, foundPublisher);
	}
	
	@Test
	public void should_find_funders() throws MappingException {
		
		Set<Funder> expectedFunders = new HashSet<>();
		expectedFunders.add(new Funder("20.500.12657/60839","Austrian Science Fund"));
		expectedFunders.add(new Funder("20.500.12657/61833","Klaas"));
		
		Set<Funder> foundFunders = mapper.getFunders();
		
		assertTrue(foundFunders.containsAll(expectedFunders));
	}

	
	@Test
	public void should_find_fundings() throws MappingException {
		
		Set<Funding> expectedFundings = new HashSet<>();
		expectedFundings.add(Funding.builder().handleFunder("20.500.12657/61833").build());
		
		Set<Funding> foundFundings = mapper.getFundings();
		
		foundFundings.forEach(System.out::println);
		
		assertTrue(foundFundings.containsAll(expectedFundings));
	}
	

	@Test
	public void should_find_classifications() throws MappingException {
		
		Classification expectedClassification = new Classification("UYQ", "Artificial intelligence");
		Set<Classification> foundClassifications = mapper.getClassifications();
		
		//foundClassifications.forEach(System.out::println);
		
		assertTrue(foundClassifications.size()==9);
		assertTrue(foundClassifications.contains(expectedClassification));
	}

	@Test
	public void should_find_languages() throws MappingException {
		
		Set<String> expectedLanguages = new HashSet<>(Arrays.asList("fre","eng","ger"));
		Set<String> foundLanguages = mapper.getLanguages();
		
		assertTrue(foundLanguages.containsAll(expectedLanguages));
	}

	@Test
	public void should_find_other_subjects() throws MappingException {
		
		Set<String> expectedSubjects = new HashSet<>(Arrays.asList("Hyperparameter Tuning", "Hyperparameters", "Tuning", "Deep Neural Networks", "Reinforcement Learning", "Machine Learning"));
		Set<String> foundSubjects = mapper.getSubjectsOther();
		
		assertTrue(foundSubjects.containsAll(expectedSubjects));
	}


	@Test
	public void should_find_contributors() throws MappingException {
		
		Set<Contributor> foundContributors = mapper.getContributors();
		
		// foundContributors.forEach(System.out::println);
		
		assertTrue(foundContributors.size()==5);
		assertTrue(foundContributors.contains(new Contributor("Zaefferer, Martin")));
	}
	

	@Test
	public void should_find_contributions() throws MappingException {
		
		Set<Contribution> foundContributions = mapper.getContributions();
		
		assertTrue(foundContributions.size()==5);
		assertTrue(foundContributions.contains(new Contribution("Gerritsen, Gerrit","author")));
		assertTrue(foundContributions.contains(new Contribution("Zaefferer, Martin","editor")));
	}
	
	
	
	@Test
	// TODO
	public void should_find_identifiers() throws MappingException {
		
		Set<Identifier> ids = mapper.getIdentifiers();
		
		// ids.forEach(System.out::println);
		
		/*
		Identifier id1 = new Identifier()
		
		Set<String> expectedSubjects = new HashSet<>(Arrays.asList("Hyperparameter Tuning", "Hyperparameters", "Tuning", "Deep Neural Networks", "Reinforcement Learning", "Machine Learning"));
		Set<String> foundSubjects = mapper.getSubjectsOther();
		
		assertTrue(foundSubjects.containsAll(expectedSubjects));
		*/
		assert(true);
	}

	
}
