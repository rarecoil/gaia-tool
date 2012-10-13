package nl.grauw.gaia_tool;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.parameters.PatchCommonTest;
import nl.grauw.gaia_tool.parameters.SystemTest;

import org.junit.Test;

public class ParametersTest {
	
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
