package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon.ArpeggioDuration;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon.ArpeggioGrid;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon.ArpeggioMotif;

import org.junit.Test;

public class ArpeggioCommonTest {

	static Address testAddress = new Address(0x10, 0x00, 0x0C, 0x00);
	static byte[] testParameterData = {
		0x08, 0x09, 0x0B, 0x3D, 0x64, 0x00, 0x01, 0x08 // 0x00
	};
	
	public static ArpeggioCommon getTestParameters() {
		return new ArpeggioCommon(testAddress, testParameterData);
	}

	@Test (expected = RuntimeException.class)
	public void testPatchArpeggioCommonParameters() {
		new ArpeggioCommon(testAddress, new byte[7]);
	}

	@Test
	public void testGetArpeggioGrid() {
		ArpeggioCommon pacp = getTestParameters();
		assertEquals(ArpeggioGrid._16t, pacp.getArpeggioGrid());
	}

	@Test
	public void testGetArpeggioDuration() {
		ArpeggioCommon pacp = getTestParameters();
		assertEquals(ArpeggioDuration.FUL, pacp.getArpeggioDuration());
	}

	@Test
	public void testGetArpeggioMotif() {
		ArpeggioCommon pacp = getTestParameters();
		assertEquals(ArpeggioMotif.PHRASE, pacp.getArpeggioMotif());
	}

	@Test
	public void testGetArpeggioOctaveRange() {
		ArpeggioCommon pacp = getTestParameters();
		assertEquals(-3, pacp.getArpeggioOctaveRange().getValue());
	}

	@Test
	public void testGetArpeggioAccentRate() {
		ArpeggioCommon pacp = getTestParameters();
		assertEquals(100, pacp.getArpeggioAccentRate().getValue());
	}

	@Test
	public void testGetArpeggioVelocity() {
		ArpeggioCommon pacp = getTestParameters();
		assertEquals(0, pacp.getArpeggioVelocity().getValue());
	}

	@Test
	public void testGetEndStep() {
		ArpeggioCommon pacp = getTestParameters();
		assertEquals(24, pacp.getEndStep().getValue());
	}

}
