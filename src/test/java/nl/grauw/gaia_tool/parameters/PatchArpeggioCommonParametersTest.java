package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters.ArpeggioDuration;
import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters.ArpeggioGrid;
import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters.ArpeggioMotif;

import org.junit.Test;

public class PatchArpeggioCommonParametersTest {

	static byte[] testAddressMap = {
		0x08, 0x09, 0x0B, 0x3D, 0x64, 0x00, 0x01, 0x08 // 0x00
	};

	@Test (expected = RuntimeException.class)
	public void testPatchArpeggioCommonParameters() {
		new PatchArpeggioCommonParameters(new byte[7]);
	}

	@Test
	public void testGetArpeggioGrid() {
		PatchArpeggioCommonParameters pacp = new PatchArpeggioCommonParameters(testAddressMap);
		assertEquals(ArpeggioGrid._16t, pacp.getArpeggioGrid());
	}

	@Test
	public void testGetArpeggioDuration() {
		PatchArpeggioCommonParameters pacp = new PatchArpeggioCommonParameters(testAddressMap);
		assertEquals(ArpeggioDuration.FUL, pacp.getArpeggioDuration());
	}

	@Test
	public void testGetArpeggioMotif() {
		PatchArpeggioCommonParameters pacp = new PatchArpeggioCommonParameters(testAddressMap);
		assertEquals(ArpeggioMotif.PHRASE, pacp.getArpeggioMotif());
	}

	@Test
	public void testGetArpeggioOctaveRange() {
		PatchArpeggioCommonParameters pacp = new PatchArpeggioCommonParameters(testAddressMap);
		assertEquals(-3, pacp.getArpeggioOctaveRange().getValue());
	}

	@Test
	public void testGetArpeggioAccentRate() {
		PatchArpeggioCommonParameters pacp = new PatchArpeggioCommonParameters(testAddressMap);
		assertEquals(100, pacp.getArpeggioAccentRate().getValue());
	}

	@Test
	public void testGetArpeggioVelocity() {
		PatchArpeggioCommonParameters pacp = new PatchArpeggioCommonParameters(testAddressMap);
		assertEquals(0, pacp.getArpeggioVelocity().getValue());
	}

	@Test
	public void testGetEndStep() {
		PatchArpeggioCommonParameters pacp = new PatchArpeggioCommonParameters(testAddressMap);
		assertEquals(24, pacp.getEndStep().getValue());
	}

}
