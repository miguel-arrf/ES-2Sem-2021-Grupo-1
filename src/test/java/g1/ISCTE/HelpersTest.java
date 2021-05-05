package g1.ISCTE;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelpersTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGetExtensionByStringHandling() {
		
		Optional<String> testFile = Helpers.getExtensionByStringHandling("test.txt");
		
		assertTrue(Helpers.getExtensionByStringHandling("test.txt").isPresent());
		assertEquals(Helpers.getExtensionByStringHandling("test.abc").get(), "abc");
		
	}

}
