/**
 * 
 */
package nl.grauw.gaia_tool;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class LogTest {

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Log#getLog()}.
	 */
	@Test
	public void testGetLog() {
		Log l = new Log();
		assertTrue("".equals(l.getLog()));
	}

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Log#log(java.lang.String)}.
	 */
	@Test
	public void testLog() {
		Log l = new Log();
		l.log("test1");
		l.log("test2");
		assertTrue("test1\ntest2\n".equals(l.getLog()));
	}

}
