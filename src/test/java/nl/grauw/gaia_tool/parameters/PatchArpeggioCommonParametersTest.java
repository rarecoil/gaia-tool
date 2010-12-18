package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.ParameterData;
import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters.ArpeggioDuration;
import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters.ArpeggioGrid;
import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters.ArpeggioMotif;

import org.junit.Test;

public class PatchArpeggioCommonParametersTest {

	static Address testAddress = new Address(0x10, 0x00, 0x0C, 0x00);
	static byte[] testParameterData = {
		0x08, 0x09, 0x0B, 0x3D, 0x64, 0x00, 0x01, 0x08 // 0x00
	};
	
	public static PatchArpeggioCommonParameters getTestParameters() {
		ParameterData data = new ParameterData(testAddress, testParameterData);
		return new PatchArpeggioCommonParameters(data);
	}

	@Test (expected = RuntimeException.class)
	public void testPatchArpeggioCommonParameters() {
		new PatchArpeggioCommonParameters(new ParameterData(testAddress, new byte[7]));
	}

	@Test
	public void testGetArpeggioGrid() {
		PatchArpeggioCommonParameters pacp = getTestParameters();
		assertEquals(ArpeggioGrid._16t, pacp.getArpeggioGrid());
	}

	@Test
	public void testGetArpeggioDuration() {
		PatchArpeggioCommonParameters pacp = getTestParameters();
		assertEquals(ArpeggioDuration.FUL, pacp.getArpeggioDuration());
	}

	@Test
	public void testGetArpeggioMotif() {
		PatchArpeggioCommonParameters pacp = getTestParameters();
		assertEquals(ArpeggioMotif.PHRASE, pacp.getArpeggioMotif());
	}

	@Test
	public void testGetArpeggioOctaveRange() {
		PatchArpeggioCommonParameters pacp = getTestParameters();
		assertEquals(-3, pacp.getArpeggioOctaveRange().getValue());
	}

	@Test
	public void testGetArpeggioAccentRate() {
		PatchArpeggioCommonParameters pacp = getTestParameters();
		assertEquals(100, pacp.getArpeggioAccentRate().getValue());
	}

	@Test
	public void testGetArpeggioVelocity() {
		PatchArpeggioCommonParameters pacp = getTestParameters();
		assertEquals(0, pacp.getArpeggioVelocity().getValue());
	}

	@Test
	public void testGetEndStep() {
		PatchArpeggioCommonParameters pacp = getTestParameters();
		assertEquals(24, pacp.getEndStep().getValue());
	}

}
