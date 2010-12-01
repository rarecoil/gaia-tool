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
public class AddressTest {

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Address#Address(int, int, int, int)}.
	 */
	@Test
	public void testAddressIntIntIntInt() {
		Address a = new Address(0x01, 0x23, 0x45, 0x67);
		assertEquals(0x0028E2E7, a.getValue());
	}

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Address#Address(int)}.
	 */
	@Test
	public void testAddressInt() {
		Address a = new Address(0x0028E2E7);
		assertEquals(0x01, a.getByte1());
		assertEquals(0x23, a.getByte2());
		assertEquals(0x45, a.getByte3());
		assertEquals(0x67, a.getByte4());
	}
	
	public void testToHexString() {
		Address a = new Address(0x0028E2E7);
		assertEquals("01 23 45 67", a.toHexString());
	}

}
