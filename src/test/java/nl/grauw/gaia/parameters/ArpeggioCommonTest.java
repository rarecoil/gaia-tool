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
package nl.grauw.gaia.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia.Address;
import nl.grauw.gaia.parameters.ArpeggioCommon;
import nl.grauw.gaia.parameters.ArpeggioCommon.ArpeggioDuration;
import nl.grauw.gaia.parameters.ArpeggioCommon.ArpeggioGrid;
import nl.grauw.gaia.parameters.ArpeggioCommon.ArpeggioMotif;

import org.junit.Test;

public class ArpeggioCommonTest {

	static Address testAddress = new Address(0x10, 0x00, 0x0C, 0x00);
	static byte[] testParameterData = {
		0x08, 0x09, 0x0B, 0x3D, 0x64, 0x00, 0x01, 0x08 // 0x00
	};
	
	public static ArpeggioCommon createTestParameters() {
		return new ArpeggioCommon(testAddress, testParameterData.clone());
	}

	@Test (expected = RuntimeException.class)
	public void testPatchArpeggioCommonParameters() {
		new ArpeggioCommon(testAddress, new byte[7]);
	}

	@Test
	public void testGetArpeggioGrid() {
		ArpeggioCommon pacp = createTestParameters();
		assertEquals(ArpeggioGrid._16t, pacp.getArpeggioGrid().getValue());
	}

	@Test
	public void testGetArpeggioDuration() {
		ArpeggioCommon pacp = createTestParameters();
		assertEquals(ArpeggioDuration.FUL, pacp.getArpeggioDuration().getValue());
	}

	@Test
	public void testGetArpeggioMotif() {
		ArpeggioCommon pacp = createTestParameters();
		assertEquals(ArpeggioMotif.PHRASE, pacp.getArpeggioMotif().getValue());
	}

	@Test
	public void testGetArpeggioOctaveRange() {
		ArpeggioCommon pacp = createTestParameters();
		assertEquals(-3, pacp.getArpeggioOctaveRange().getValue());
	}

	@Test
	public void testGetArpeggioAccentRate() {
		ArpeggioCommon pacp = createTestParameters();
		assertEquals(100, pacp.getArpeggioAccentRate().getValue());
	}

	@Test
	public void testGetArpeggioVelocity() {
		ArpeggioCommon pacp = createTestParameters();
		assertEquals(0, pacp.getArpeggioVelocity().getValue());
	}

	@Test
	public void testGetEndStep() {
		ArpeggioCommon pacp = createTestParameters();
		assertEquals(24, pacp.getEndStep().getValue());
	}

}
