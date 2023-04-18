package org.oapen.memoproject.dataingestion.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.oapen.memoproject.dataingestion.jpa.entities.ExportChunk;
import org.oapen.memoproject.dataingestion.jpa.entities.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@EnabledIf(expression = "${dbreadtests.enabled}", loadContext = true)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestPropertySource(locations="/application.properties")
@DataJpaTest
public class JpaReadTests {
	
	@Autowired
	ExportChunkRepository exportChunkRepository;
	
	@Autowired
	TitleRepository titleRepository;
	
	@Test
	public void test_can_find_exportChunks_for_handle() {
		
		JpaPersistenceService service = new JpaPersistenceService();
		service.setExportChunkRepository(exportChunkRepository);
		List<ExportChunk> lst = service.getExportChunks("20.500.12657/45638");
		//lst.forEach(System.out::println);
		assertTrue(lst.size()==4);
	}
	
	@Test
	public void test_can_find_title_for_handle() {
		
		Optional<Title> q = titleRepository.findById("20.500.12657/22262");
		assertTrue(q.isPresent());
	}
	

}
