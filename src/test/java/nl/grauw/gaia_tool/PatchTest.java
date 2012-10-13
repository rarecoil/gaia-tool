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
	
	public Patch createTestPatch() {
		Patch patch = new Patch();
		patch.setCommon(PatchCommonTest.createTestParameters());
		for (int tone = 1; tone <= 3; tone++)
			patch.setTone(tone, ToneTest.createTestParameters());
		patch.setDistortion(DistortionTest.createTestParameters());
		patch.setFlanger(FlangerTest.createTestParameters());
		patch.setDelay(DelayTest.createTestParameters());
		patch.setReverb(ReverbTest.createTestParameters());
		patch.setArpeggioCommon(ArpeggioCommonTest.createTestParameters());
		for (int note = 1; note <= 16; note++)
			patch.setArpeggioPattern(note, ArpeggioPatternTest.createTestParameters());
		return patch;
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
