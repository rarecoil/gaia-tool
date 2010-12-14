/**
 * 
 */
package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.parameters.PatchCommonParameters.DBeamAssign;
import nl.grauw.gaia_tool.parameters.PatchCommonParameters.DBeamPolarity;
import nl.grauw.gaia_tool.parameters.PatchCommonParameters.SyncRingSelect;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class PatchCommonParametersTest {
	
	static byte[] testAddressMap = {
		'T', 'e', 's', 't', ' ', 'P', 'a', 't',         // 0x00
		'c', 'h', '-', '-', 0x7F, 0x01, 0x02, 0x04,     // 0x08
		0x00, 0x01, 0x00, 0x7E, 0x01, 0x43, 0x18, 0x17, // 0x10
		0x00, 0x01, 0x00, 0x00, 0x01, 0x01, 0x00, 0x02, // 0x18
		0x01, 0x03, 0x00, 0x01, 0x1C, 0x01, 0x00, 0x01, // 0x20
		0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, // 0x28
		0x00, 0x01, 0x00, 0x01, 0x7D, 0x7C, 0x7B, 0x7A, // 0x30
		0x79, 0x78, 0x3F, 0x3E, 0x3D // 0x38
	};
	
	@Test (expected = RuntimeException.class)
	public void testPatchCommonParameters() {
		new PatchCommonParameters(new byte[60]);
	}

	@Test
	public void testGetPatchName() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertTrue("Test Patch--".equals(pcp.getPatchName()));
	}

	@Test
	public void testGetPatchLevel() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(127, pcp.getPatchLevel().getValue());
	}

	@Test
	public void testGetPatchTempo() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(292, pcp.getPatchTempo().getValue());
	}

	@Test
	public void testGetArpeggioSwitch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(false, pcp.getArpeggioSwitch());
	}

	@Test
	public void testGetReserved1() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(1, pcp.getReserved1().getValue());
	}

	@Test
	public void testGetPortamentoSwitch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(false, pcp.getPortamentoSwitch());
	}

	@Test
	public void testGetPortamentoTime() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(126, pcp.getPortamentoTime().getValue());
	}

	@Test
	public void testGetMonoSwitch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(true, pcp.getMonoSwitch());
	}

	@Test
	public void testGetOctaveShift() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(3, pcp.getOctaveShift().getValue());
	}

	@Test
	public void testGetPitchBendRangeUp() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(24, pcp.getPitchBendRangeUp().getValue());
	}

	@Test
	public void testGetPitchBendRangeDown() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(23, pcp.getPitchBendRangeDown().getValue());
	}

	@Test
	public void testGetReserved2() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(0, pcp.getReserved2().getValue());
	}

	@Test
	public void testGetTone1Switch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(true, pcp.getTone1Switch());
	}

	@Test
	public void testGetTone1Select() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(false, pcp.getTone1Select());
	}

	@Test
	public void testGetTone2Switch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(false, pcp.getTone2Switch());
	}

	@Test
	public void testGetTone2Select() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(true, pcp.getTone2Select());
	}

	@Test
	public void testGetTone3Switch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(true, pcp.getTone3Switch());
	}

	@Test
	public void testGetTone3Select() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(false, pcp.getTone3Select());
	}

	@Test
	public void testGetSyncRingSelect() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(SyncRingSelect.RING, pcp.getSyncRingSelect());
	}

	@Test
	public void testGetEffectsMasterSwitch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(true, pcp.getEffectsMasterSwitch());
	}

	@Test
	public void testGetReserved3() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(3, pcp.getReserved3().getValue());
	}

	@Test
	public void testGetDelayTempoSyncSwitch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(false, pcp.getDelayTempoSyncSwitch());
	}

	@Test
	public void testGetLowBoostSwitch() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(true, pcp.getLowBoostSwitch());
	}

	@Test
	public void testGetDBeamAssign() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(DBeamAssign.FILTER_CUTOFF_KF, pcp.getDBeamAssign());
	}

	@Test
	public void testGetReserved4() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(1, pcp.getReserved4().getValue());
	}

	@Test
	public void testGetReserved5() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(0, pcp.getReserved5().getValue());
	}

	@Test
	public void testGetReserved6() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(1, pcp.getReserved6().getValue());
	}

	@Test
	public void testGetReserved7() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(0, pcp.getReserved7().getValue());
	}

	@Test
	public void testGetDBeamPolarity() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(DBeamPolarity.REVERSE, pcp.getDBeamPolarity());
	}

	@Test
	public void testGetEffectsDistortionSelect() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(false, pcp.getEffectsDistortionSelect());
	}

	@Test
	public void testGetEffectsFlangerSelect() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(true, pcp.getEffectsFlangerSelect());
	}

	@Test
	public void testGetEffectsDelaySelect() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(false, pcp.getEffectsDelaySelect());
	}

	@Test
	public void testGetEffectsReverbSelect() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(true, pcp.getEffectsReverbSelect());
	}

	@Test
	public void testGetReserved8() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(0, pcp.getReserved8().getValue());
	}

	@Test
	public void testGetReserved9() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(1, pcp.getReserved9().getValue());
	}

	@Test
	public void testGetReserved10() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(0, pcp.getReserved10().getValue());
	}

	@Test
	public void testGetReserved11() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(1, pcp.getReserved11().getValue());
	}

	@Test
	public void testGetReserved12() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(0, pcp.getReserved12().getValue());
	}

	@Test
	public void testGetReserved13() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(1, pcp.getReserved13().getValue());
	}

	@Test
	public void testGetReserved14() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(125, pcp.getReserved14().getValue());
	}

	@Test
	public void testGetReserved15() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(124, pcp.getReserved15().getValue());
	}

	@Test
	public void testGetReserved16() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(123, pcp.getReserved16().getValue());
	}

	@Test
	public void testGetReserved17() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(122, pcp.getReserved17().getValue());
	}

	@Test
	public void testGetReserved18() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(121, pcp.getReserved18().getValue());
	}

	@Test
	public void testGetReserved19() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(120, pcp.getReserved19().getValue());
	}

	@Test
	public void testGetReserved20() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(-1, pcp.getReserved20().getValue());
	}

	@Test
	public void testGetReserved21() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(-2, pcp.getReserved21().getValue());
	}

	@Test
	public void testGetReserved22() {
		PatchCommonParameters pcp = new PatchCommonParameters(testAddressMap);
		assertEquals(-3, pcp.getReserved22().getValue());
	}

}
