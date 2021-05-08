package g1.ISCTE;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TableHolderTest {

	@BeforeAll
	public static void test() {
		getCoverage();

	}
	
	private static void getCoverage() {
		TableHolder tableHolder = new TableHolder("nom", "loc", "wmc", "loc_method", "cyclo");
	}
	

	@Test
	void testGetNOM_class() {
		TableHolder tableHolder = new TableHolder("nom", "loc", "wmc", "loc_method", "cyclo");
		assertEquals(tableHolder.getNOM_class(), "nom");
	}

	@Test
	void testGetLOC_class() {
		TableHolder tableHolder = new TableHolder("nom", "loc", "wmc", "loc_method", "cyclo");
		assertEquals(tableHolder.getLOC_class(), "loc");
	}

	@Test
	void testGetWMC_class() {
		TableHolder tableHolder = new TableHolder("nom", "loc", "wmc", "loc_method", "cyclo");
		assertEquals(tableHolder.getWMC_class(), "wmc");
	}

	@Test
	void testGetLOC_method() {
		TableHolder tableHolder = new TableHolder("nom", "loc", "wmc", "loc_method", "cyclo");
		assertEquals(tableHolder.getLOC_method(), "loc_method");
	}

	@Test
	void testGetCYCLO_method() {
		TableHolder tableHolder = new TableHolder("nom", "loc", "wmc", "loc_method", "cyclo");
		assertEquals(tableHolder.getCYCLO_method(), "cyclo");
	}

}
