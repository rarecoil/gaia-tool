/*
 * Copyright 2010 Laurens Holst
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.grauw.gaia_tool;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddressTest {

	@Test
	public void testAddressIntIntIntInt() {
		Address a = new Address(0x01, 0x23, 0x45, 0x67);
		assertEquals(0x0028E2E7, a.getValue());
	}

	@Test
	public void testAddressInt() {
		Address a = new Address(0x0028E2E7);
		assertEquals(0x01, a.getByte1());
		assertEquals(0x23, a.getByte2());
		assertEquals(0x45, a.getByte3());
		assertEquals(0x67, a.getByte4());
	}
	
	@Test
	public void testAddInt() {
		Address a = new Address(0x0028E2E7);
		Address b = a.add(-0x123456);
		assertEquals(0x16AE91, b.getValue());
	}
	
	@Test
	public void testToHexString() {
		Address a = new Address(0x0028E2E7);
		assertEquals("01 23 45 67", a.toHexString());
	}

}
