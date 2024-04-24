package org.oapen.memoproject.dataingestion.harvest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.Classification;
import org.oapen.memoproject.dataingestion.jpa.entities.Contribution;
import org.oapen.memoproject.dataingestion.jpa.entities.Contributor;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Funder;
import org.oapen.memoproject.dataingestion.jpa.entities.GrantData;
import org.oapen.memoproject.dataingestion.jpa.entities.Identifier;
import org.oapen.memoproject.dataingestion.jpa.entities.Publisher;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XOAIDocumentParserTests {
	
	private final String xmlrecord1 = TestConstants.xmlrecord1;
	private final String xmlrecordDelete = TestConstants.xmlrecordDelete;
	
	private XOAIDocumentParser source1;
	private XOAIDocumentParser sourceDelete;

	
	@BeforeEach
    void setUp() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		Document doc1 = db.parse(new InputSource( new StringReader( xmlrecord1 ) ));
		Element el1 = (Element) doc1.getElementsByTagName("record").item(0);
		
		source1 = new XOAIDocumentParser(el1);
		
		Document doc2 = db.parse(new InputSource( new StringReader( xmlrecordDelete ) ));
		Element el2 = (Element) doc2.getElementsByTagName("record").item(0);
		
		sourceDelete = new XOAIDocumentParser(el2);
	}	
	

	@Test 
	public void should_find_status_deleted() {
		
		assertEquals("deleted", sourceDelete.getTitle().get().getStatus());
	}

	@Test 
	public void should_find_no_status_on_regular_record() {
		
		assertTrue(source1.getTitle().get().getStatus()==null);
	}

	@Test
	public void should_find_handler() {

		assertEquals("20.500.12657/60840", source1.getTitle().get().getHandle());
	}
	
	@Test
	public void should_find_sysId() {

		assertEquals("22222222-3aff-4e7b-9a1c-ce8c75fa9530", source1.getTitle().get().getSysId());
	}

	@Test
	public void should_find_collections() {

		Set<String> expectedCollections = new HashSet<>(
			Arrays.asList(
				"Toward an Open Monograph Ecosystem (TOME)",
				"Knowledge Unlatched (KU)"
			)	
		);
		Set<String> foundCollections = source1.getTitle().get().getCollections();
		
		assertTrue(foundCollections.containsAll(expectedCollections));
	}

	@Test
	public void should_find_downloadUrl() {

		assertEquals("https://library.oapen.org/bitstream/20.500.12657/60840/1/978-981-19-5170-1.pdf", source1.getTitle().get().getDownloadUrl());
	}
	
	@Test
	public void should_find_thumbnail() {

		assertEquals("https://library.oapen.org/bitstream/20.500.12657/60840/7/978-981-19-5170-1.pdf.jpg", source1.getTitle().get().getThumbnail());
	}

	@Test
	public void should_find_license() {

		assertEquals("http://creativecommons.org/licenses/by/4.0/", source1.getTitle().get().getLicense());
	}

	@Test
	public void should_find_webshopUrl() {

		assertEquals("https://link.springer.com/978-981-19-5170-1", source1.getTitle().get().getWebshopUrl());
	}

	@Test
	public void should_find_yearAvailable() {

		assertEquals(source1.getTitle().get().getYearAvailable(),2010);
	}
	
	@Test
	public void should_find_descriptionOtherlanguage() {

		assertEquals("Just a test string", source1.getTitle().get().getDescriptionOtherLanguage());
	}
	
	@Test
	public void should_find_descriptionAbstract() {

		assertTrue(source1.getTitle().get().getDescriptionAbstract().startsWith("This open access book"));
	}
	
	@Test
	public void should_find_termsAbstract() {

		assertTrue(source1.getTitle().get().getTermsAbstract().startsWith("Simplified Signs presents"));
	}
	
	@Test
	public void should_find_abstractOtherLanguage() {

		assertTrue(source1.getTitle().get().getAbstractOtherLanguage().startsWith("Während der Zeit des historischen Kolonialismus"));
	}

	@Test
	public void should_find_partOfSeries() {

		assertEquals("Cultural Heritage Studies", source1.getTitle().get().getPartOfSeries());
	}
	
	@Test
	public void should_find_title() {

		assertEquals("Hyperparameter Tuning for Machine and Deep Learning with R", source1.getTitle().get().getTitle());
	}

	@Test
	public void should_find_titleAlternative() {

		assertEquals("A Practical Guide", source1.getTitle().get().getTitleAlternative());
	}
	
	@Test
	public void should_find_type() {

		assertEquals("book", source1.getTitle().get().getType());
	}
	
	@Test
	public void should_find_chapterNumber() {

		assertEquals("23", source1.getTitle().get().getChapterNumber());
	}

	@Test
	public void should_find_imprint() {

		assertEquals("Springer Nature Singapore", source1.getTitle().get().getImprint());
	}
	
	@Test
	public void should_find_pages() {

		assertEquals("323", source1.getTitle().get().getPages());
	}
	
	@Test
	public void should_find_placePublication() {

		assertEquals("Singapore", source1.getTitle().get().getPlacePublication());
	}
	
	@Test
	public void should_find_seriesNumber() {

		assertEquals("12345", source1.getTitle().get().getSeriesNumber());
	}
	
	@Test
	public void should_find_partOfBook() {

		assertEquals("20.500.12657/48278", source1.getTitle().get().getPartOfBook());
	}
	
	
	
	@Test
	public void should_find_publisher() {
		
		Publisher expectedPublisher = new Publisher("20.500.12657/22488","Springer Nature "); // watch the space
		Publisher foundPublisher = source1.getPublisher().get();
		
		assertEquals(expectedPublisher.getHandle(), foundPublisher.getHandle());
		assertEquals(expectedPublisher.getName(), foundPublisher.getName());
	}
	
	@Test
	public void should_find_funders() {
		
		Set<Funder> expectedFunders = new HashSet<>();
		expectedFunders.add(new Funder("20.500.12657/60839","Only handle matters for equals!"));
		expectedFunders.add(new Funder("20.500.12657/61833","Only handle matters for equals!"));
		
		Set<Funder> funderSet = source1.getFunders();
		
		ArrayList<Funder> foundFunders = new ArrayList<Funder>(funderSet);
		
		/*foundFunders.forEach(f -> {
			System.out.println(f);
			System.out.println("ACR " + f.getAcronyms());
		});*/
		
		assertTrue(foundFunders.containsAll(expectedFunders));
	}

	
	@Test
	public void should_find_funder_acronyms() {
		
		Set<Funder> funderSet = source1.getFunders();
		ArrayList<Funder> foundFunders = new ArrayList<Funder>(funderSet);
		
		assertEquals(foundFunders.get(0).getAcronyms().size(), 5);
	}
	
	
	
	@Test
	public void should_find_grant_data() {
		
		Set<GrantData> foundGrants = source1.getTitle().get().getGrantData();
		
		assertEquals(foundGrants.size(), 2);
		
		List<String> values = foundGrants.stream()
				.map(g -> g.getValue())
				.collect(Collectors.toList());
		
		assertTrue( values.contains("10BP12_185527"));
		assertTrue( values.contains("Open Access Books") );
	}
	

	@Test
	public void should_find_classifications() {
		
		Classification expectedClassification = new Classification("UYQ", "Artificial intelligence");
		Set<Classification> foundClassifications = source1.getClassifications();
		
		assertEquals(foundClassifications.size(), 9);
		assertTrue(foundClassifications.contains(expectedClassification));
	}

	@Test
	public void should_find_languages() {
		
		Set<String> expectedLanguages = new HashSet<>(Arrays.asList("fre","eng","ger"));
		Set<String> foundLanguages = source1.getTitle().get().getLanguages();
		
		assertTrue(foundLanguages.containsAll(expectedLanguages));
	}

	@Test
	public void should_find_other_subjects() {
		
		Set<String> expectedSubjects = new HashSet<>(Arrays.asList("Hyperparameter Tuning", "Hyperparameters", "Tuning", "Deep Neural Networks", "Reinforcement Learning", "Machine Learning"));
		Set<String> foundSubjects = source1.getTitle().get().getSubjectsOther();
		
		assertTrue(foundSubjects.containsAll(expectedSubjects));
	}


	@Test
	public void should_find_contributors() {
		
		Set<Contributor> foundContributors = source1.getContributors();
		
		// foundContributors.forEach(System.out::println);
		
		assertEquals(foundContributors.size(), 5);
		assertTrue(foundContributors.contains(new Contributor("Zaefferer, Martin")));
	}
	

	@Test
	public void should_find_contributions() {
		
		Set<Contribution> foundContributions = source1.getTitle().get().getContributions();
		
		assertEquals(foundContributions.size(), 5);
		
		List<String> names = foundContributions.stream()
				.map(c -> c.getContributorName())
				.collect(Collectors.toList());
		
		assertTrue( names.contains("Gerritsen, Gerrit")	);
		assertTrue( names.contains("Zaefferer, Martin") );
	}
	
	
	@Test
	public void should_find_identifiers() {
		
		Title title = source1.getTitle().get();
		
		Set<Identifier> ids = title.getIdentifiers();
		
		// ids.forEach(System.out::println);
		
		assertEquals(4, ids.stream().filter(id -> id.getType().equals("ISBN")).count());
		assertEquals(2, ids.stream().filter(id -> id.getType().equals("OCN")).count());
		assertEquals(1, ids.stream().filter(id -> id.getType().equals("ONIX")).count());
		assertEquals(1, ids.stream().filter(id -> id.getType().equals("ISSN")).count());
		assertEquals(1, ids.stream().filter(id -> id.getType().equals("DOI")).count());
		assertEquals(1, ids.stream().filter(id -> id.getType().equals("URI")).count());
	}

	@Test
	public void should_find_exportChunks() {
		
		Set<ExportChunk> chunks = source1.getTitle().get().getExportChunks();

		assertEquals(chunks.size(),4);
	}
	
	
}
