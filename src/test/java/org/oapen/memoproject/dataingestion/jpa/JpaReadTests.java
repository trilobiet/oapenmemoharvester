package org.oapen.memoproject.dataingestion.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
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
	TitleRepository titleRepository;
	
	@Test
	public void test_can_find_title_for_handle() {
		
		Optional<Title> q = titleRepository.findById("20.500.12657/22262");
		assertTrue(q.isPresent());
	}
	
	
	// @Test
	public void t1() {
		
		Optional<Title> q = titleRepository.findById("hndl3");
		assertTrue(q.isPresent());
		assertTrue(q.get().getIdentifiers().size() == 3);
	}

}
