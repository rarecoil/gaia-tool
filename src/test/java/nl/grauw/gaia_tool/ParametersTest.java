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

import nl.grauw.gaia_tool.parameters.PatchCommonTest;
import nl.grauw.gaia_tool.parameters.SystemTest;

import org.junit.Test;

public class ParametersTest {

	@Test
	public void testGetValue() {
		Parameters p = new Parameters(new Address(0, 0, 0, 0), new byte[] { 0x56, (byte)0x93 });
		
		assertEquals(0x56, p.getValue(0));
		assertEquals(0x13, p.getValue(1));
	}
	
	@Test
	public void testGet8BitValue() {
		Parameters p = new Parameters(new Address(0, 0, 0, 0), new byte[] { 0x08, 0x04, (byte)0xF1, 0x03 });
		
		assertEquals(0x84, p.get8BitValue(0));
		assertEquals(0x41, p.get8BitValue(1));
		assertEquals(0x13, p.get8BitValue(2));
	}
	
	@Test
	public void testGet12BitValue() {
		Parameters p = new Parameters(new Address(0, 0, 0, 0), new byte[] { 0x08, 0x04, 0x02, (byte)0xF1, 0x03, 0x07 });
		
		assertEquals(0x842, p.get12BitValue(0));
		assertEquals(0x421, p.get12BitValue(1));
		assertEquals(0x213, p.get12BitValue(2));
		assertEquals(0x137, p.get12BitValue(3));
	}
	
	@Test
	public void testGet16BitValue() {
		Parameters p = new Parameters(new Address(0, 0, 0, 0), new byte[] { 0x08, 0x04, 0x02, 0x01, (byte)0xF1, 0x03, 0x07, 0x0F });
		
		assertEquals(0x8421, p.get16BitValue(0));
		assertEquals(0x4211, p.get16BitValue(1));
		assertEquals(0x2113, p.get16BitValue(2));
		assertEquals(0x1137, p.get16BitValue(3));
		assertEquals(0x137F, p.get16BitValue(4));
	}
	
	@Test
	public void testIsEqualTo() {
		Parameters p1 = PatchCommonTest.createTestParameters();
		Parameters p2 = PatchCommonTest.createTestParameters();
		
		assertEquals(true, p1.isEqualTo(p2));
	}
	
	@Test
	public void testIsEqualTo_DifferentParameter() {
		Parameters p1 = PatchCommonTest.createTestParameters();
		Parameters p2 = SystemTest.createTestParameters();
		
		assertEquals(false, p1.isEqualTo(p2));
	}

	@Test
	public void testIsEqualTo_DifferentParameterData() {
		Parameters p1 = PatchCommonTest.createTestParameters();
		Parameters p2 = PatchCommonTest.createTestParameters();
		p2.setValue(0x0B, '!');
		
		assertEquals(false, p1.isEqualTo(p2));
	}
	
	@Test
	public void testIsEqualTo_DifferentPatchAddress() {
		Parameters p1 = PatchCommonTest.createTestParameters();
		Parameters p2 = new Parameters(new Address(0x20, 0x09, 0x00, 0x00), PatchCommonTest.createTestParameters().getData());
		
		assertEquals(true, p1.isEqualTo(p2));
	}
	
	@Test
	public void testIsEqualTo_DifferentParameterAddress() {
		Parameters p1 = PatchCommonTest.createTestParameters();
		Parameters p2 = new Parameters(new Address(0x20, 0x00, 0x01, 0x00), PatchCommonTest.createTestParameters().getData());
		
		assertEquals(false, p1.isEqualTo(p2));
	}
	
}
