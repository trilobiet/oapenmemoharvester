package org.oapen.memoproject.dataingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

public class ElementToEntitiesMapperTests {
	
	ElementToEntitiesMapper mapper1;
	ElementToEntitiesMapper mapperToDelete;
	
	@BeforeEach
    void setUp() throws Exception {

		final String s = "./src/test/resources/xoai-response-short.xml";
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document xml = db.parse(s);
		
		ListRecordsDocument lrdoc = new ListRecordsDocumentImp(xml);
		
		/* 
		 * Test document contains 4 records (0,1,2,3)
		 * Record 3 contains no data
		 */
		Element record1 = lrdoc.getRecords().get(1);
		Element recordToDelete = lrdoc.getRecords().get(3);
		
		this.mapper1 = new XpathElementToEntitiesMapper(record1);
		this.mapperToDelete = new XpathElementToEntitiesMapper(recordToDelete);
	}	
	
	
	@Test 
	public void should_find_status_deleted() {
		
		assertEquals("deleted", mapperToDelete.getStatus().get());
	}

	@Test 
	public void should_find_no_status_on_regular_record() {
		
		assertTrue(mapper1.getStatus().isEmpty());
	}

	@Test
	public void should_find_handler() {

		assertEquals("20.500.12657/60840", mapper1.getHandle().get());
		assertEquals("20.500.12657/44456", mapperToDelete.getHandle().get());
	}
	
	@Test
	public void should_find_sysId() {

		assertEquals("22222222-3aff-4e7b-9a1c-ce8c75fa9530", mapper1.getSysId().get());
	}

	@Test
	public void should_find_collection() {

		assertEquals("col_20.500.12657_6", mapper1.getCollection().get());
	}

	@Test
	public void should_find_downloadUrl() {

		assertEquals("https://library.oapen.org/bitstream/20.500.12657/60840/1/978-981-19-5170-1.pdf", mapper1.getDownloadUrl().get());
	}
	
	@Test
	public void should_find_thumbnail() {

		assertEquals("https://library.oapen.org/bitstream/20.500.12657/60840/7/978-981-19-5170-1.pdf.jpg", mapper1.getThumbnail().get());
	}

	@Test
	public void should_find_license() {

		assertEquals("http://creativecommons.org/licenses/by/4.0/", mapper1.getLicense().get());
	}

	@Test
	public void should_find_webshopUrl() {

		assertEquals("https://link.springer.com/978-981-19-5170-1", mapper1.getWebshopUrl().get());
	}

	@Test
	public void should_find_yearAvailable() {

		assertEquals(mapper1.getYearAvailable().get(),2010);
	}
	
	@Test
	public void should_find_descriptionOtherlanguage() {

		assertEquals("Just a test string", mapper1.getDescriptionOtherLanguage().get());
	}
	
	@Test
	public void should_find_descriptionAbstract() {

		assertTrue(mapper1.getDescriptionAbstract().get().startsWith("This open access book"));
	}
	
	@Test
	public void should_find_termsAbstract() {

		assertTrue(mapper1.getTermsAbstract().get().startsWith("Simplified Signs presents"));
	}
	
	@Test
	public void should_find_abstractOtherLanguage() {

		assertTrue(mapper1.getAbstractOtherLanguage().get().startsWith("Während der Zeit des historischen Kolonialismus"));
	}

	@Test
	public void should_find_partOfSeries() {

		assertEquals("Cultural Heritage Studies", mapper1.getPartOfSeries().get());
	}
	
	@Test
	public void should_find_title() {

		assertEquals("Hyperparameter Tuning for Machine and Deep Learning with R", mapper1.getTitle().get());
	}

	@Test
	public void should_find_titleAlternative() {

		assertEquals("A Practical Guide", mapper1.getTitleAlternative().get());
	}
	
	@Test
	public void should_find_type() {

		assertEquals("book", mapper1.getType().get());
	}
	
	@Test
	public void should_find_chapterNumber() {

		assertEquals("23", mapper1.getChapterNumber().get());
	}

	@Test
	public void should_find_imprint() {

		assertEquals("Springer Nature Singapore", mapper1.getImprint().get());
	}
	
	@Test
	public void should_find_pages() {

		assertEquals("323", mapper1.getPages().get());
	}
	
	@Test
	public void should_find_placePublication() {

		assertEquals("Singapore", mapper1.getPlacePublication().get());
	}
	
	@Test
	public void should_find_seriesNumber() {

		assertEquals("12345", mapper1.getSeriesNumber().get());
	}
	
	@Test
	public void should_find_partOfBook() {

		assertEquals("20.500.12657/48278", mapper1.getPartOfBook().get());
	}
	
	
	
	@Test
	public void should_find_publisher() {
		
		Publisher expectedPublisher = new Publisher("20.500.12657/22488","Springer Nature");
		Publisher foundPublisher = mapper1.getPublisher().get();
		
		assertEquals(expectedPublisher, foundPublisher);
	}
	
	@Test
	public void should_find_funders() {
		
		Set<Funder> expectedFunders = new HashSet<>();
		expectedFunders.add(new Funder("20.500.12657/60839","Austrian Science Fund"));
		expectedFunders.add(new Funder("20.500.12657/61833","Klaas"));
		
		Set<Funder> foundFunders = mapper1.getFunders();
		
		// foundFunders.forEach(f -> System.out.println(f.getAcronyms()));
		
		assertTrue(foundFunders.containsAll(expectedFunders));
	}

	
	@Test
	public void should_find_grant_data() {
		
		Set<GrantData> expectedGrantData = new HashSet<>();
		expectedGrantData.add(new GrantData("number","10BP12_185527"));
		expectedGrantData.add(new GrantData("program","Open Access Books"));
		
		Set<GrantData> foundGrants = mapper1.getGrantData();
		
		// foundGrants.forEach(System.out::println);
		
		assertTrue(foundGrants.containsAll(expectedGrantData));
	}
	

	@Test
	public void should_find_classifications() {
		
		Classification expectedClassification = new Classification("UYQ", "Artificial intelligence");
		Set<Classification> foundClassifications = mapper1.getClassifications();
		
		//foundClassifications.forEach(System.out::println);
		
		assertTrue(foundClassifications.size()==9);
		assertTrue(foundClassifications.contains(expectedClassification));
	}

	@Test
	public void should_find_languages() {
		
		Set<String> expectedLanguages = new HashSet<>(Arrays.asList("fre","eng","ger"));
		Set<String> foundLanguages = mapper1.getLanguages();
		
		assertTrue(foundLanguages.containsAll(expectedLanguages));
	}

	@Test
	public void should_find_other_subjects() {
		
		Set<String> expectedSubjects = new HashSet<>(Arrays.asList("Hyperparameter Tuning", "Hyperparameters", "Tuning", "Deep Neural Networks", "Reinforcement Learning", "Machine Learning"));
		Set<String> foundSubjects = mapper1.getSubjectsOther();
		
		assertTrue(foundSubjects.containsAll(expectedSubjects));
	}


	@Test
	public void should_find_contributors() {
		
		Set<Contributor> foundContributors = mapper1.getContributors();
		
		// foundContributors.forEach(System.out::println);
		
		assertTrue(foundContributors.size()==5);
		assertTrue(foundContributors.contains(new Contributor("Zaefferer, Martin")));
	}
	

	@Test
	public void should_find_contributions() {
		
		Set<Contribution> foundContributions = mapper1.getContributions();
		
		assertTrue(foundContributions.size()==5);
		assertTrue(foundContributions.contains(new Contribution("Gerritsen, Gerrit","author")));
		assertTrue(foundContributions.contains(new Contribution("Zaefferer, Martin","editor")));
	}
	
	
	@Test
	public void should_find_identifiers() {
		
		Set<Identifier> ids = mapper1.getIdentifiers();
		
		//ids.forEach(System.out::println);
		
		Identifier id1 = new Identifier("9789811951701","ISBN");
		
		assertTrue(ids.size()==9);
		assertTrue(ids.contains(id1));
	}

	@Test
	public void should_find_exportChunks() {
		
		Set<ExportChunk> chunks = mapper1.getExportChunks();

		//chunks.forEach(System.out::println);

		assertTrue(chunks.size()==4);
	}
	
	
	@Test
	public void should_find_title_object() {
		
		Optional<Title> title = mapper1.getItem();
		// System.out.println(title);
		
		assertTrue(title.isPresent());
		assertTrue(title.get().getHandle().equals("20.500.12657/60840"));
		assertTrue(title.get().getPublisher()!= null);
		assertTrue(title.get().getClassifications().size()==9);
		assertTrue(title.get().getContributions().size()==5);
		assertTrue(title.get().getExportChunks().size()==4);
		assertTrue(title.get().getFunders().size()==2);
		assertTrue(title.get().getGrantdata().size()==2);
		assertTrue(title.get().getIdentifiers().size()==9);
	}
}
