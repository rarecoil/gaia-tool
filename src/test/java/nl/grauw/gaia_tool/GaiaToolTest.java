package nl.grauw.gaia_tool;

import static org.junit.Assert.*;

import org.junit.Test;

public class GaiaToolTest {

	@Test
	public void testGaiaTool() {
		new GaiaTool();
	}

	@Test
	public void testGetLog() {
		GaiaTool tool = new GaiaTool();
		assertTrue(tool.getLog() instanceof Log);
	}

	@Test
	public void testGetGaia() {
		GaiaTool tool = new GaiaTool();
		assertTrue(tool.getGaia() instanceof Gaia);
	}

}
