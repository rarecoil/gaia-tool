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

import nl.grauw.gaia_tool.parameters.ArpeggioCommonTest;
import nl.grauw.gaia_tool.parameters.ArpeggioPatternTest;
import nl.grauw.gaia_tool.parameters.DelayTest;
import nl.grauw.gaia_tool.parameters.DistortionTest;
import nl.grauw.gaia_tool.parameters.FlangerTest;
import nl.grauw.gaia_tool.parameters.PatchCommonTest;
import nl.grauw.gaia_tool.parameters.ReverbTest;
import nl.grauw.gaia_tool.parameters.ToneTest;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon.ArpeggioDuration;

import org.junit.Test;

public class PatchTest {
	
	public static Patch createTestPatch() {
		Patch patch = new Patch();
		patch.setCommon(PatchCommonTest.createTestParameters());
		for (int tone = 1; tone <= 3; tone++)
			patch.setTone(tone, ToneTest.createTestParameters(tone));
		patch.setDistortion(DistortionTest.createTestParameters());
		patch.setFlanger(FlangerTest.createTestParameters());
		patch.setDelay(DelayTest.createTestParameters());
		patch.setReverb(ReverbTest.createTestParameters());
		patch.setArpeggioCommon(ArpeggioCommonTest.createTestParameters());
		for (int note = 1; note <= 16; note++)
			patch.setArpeggioPattern(note, ArpeggioPatternTest.createTestParameters(note));
		return patch;
	}
	
	@Test
	public void testIsComplete() {
		Patch patch = createTestPatch();
		
		assertEquals(true, patch.isComplete());
	}

	@Test
	public void testIsComplete_Incomplete() {
		Patch patch = createTestPatch();
		patch.setTone(3, null);
		
		assertEquals(false, patch.isComplete());
	}
	
	@Test
	public void testIsEqualTo() {
		Patch a = createTestPatch();
		Patch b = createTestPatch();
		
		assertEquals(true, a.isEqualTo(b));
	}
	
	@Test
	public void testIsEqualTo_ParameterDifference() {
		Patch a = createTestPatch();
		Patch b = createTestPatch();
		b.getArpeggioCommon().getArpeggioDuration().setValue(ArpeggioDuration._30);
		
		assertEquals(false, a.isEqualTo(b));
	}
	
}
