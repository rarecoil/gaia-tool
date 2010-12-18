/**
 * 
 */
package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.parameters.PatchCommon.DBeamAssign;
import nl.grauw.gaia_tool.parameters.PatchCommon.DBeamPolarity;
import nl.grauw.gaia_tool.parameters.PatchCommon.SyncRingSelect;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class PatchCommonTest {
	
	static Address testAddress = new Address(0x10, 0x00, 0x00, 0x00);
	static byte[] testParameterData = {
		'T', 'e', 's', 't', ' ', 'P', 'a', 't',         // 0x00
		'c', 'h', '-', '-', 0x7F, 0x01, 0x02, 0x04,     // 0x08
		0x00, 0x01, 0x00, 0x7E, 0x01, 0x43, 0x18, 0x17, // 0x10
		0x00, 0x01, 0x00, 0x00, 0x01, 0x01, 0x00, 0x02, // 0x18
		0x01, 0x03, 0x00, 0x01, 0x1C, 0x01, 0x00, 0x01, // 0x20
		0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, // 0x28
		0x00, 0x01, 0x00, 0x01, 0x7D, 0x7C, 0x7B, 0x7A, // 0x30
		0x79, 0x78, 0x3F, 0x3E, 0x3D // 0x38
	};
	
	public static PatchCommon getTestParameters() {
		return new PatchCommon(testAddress, testParameterData);
	}
	
	@Test (expected = RuntimeException.class)
	public void testPatchCommonParameters() {
		new PatchCommon(testAddress, new byte[60]);
	}

	@Test
	public void testGetPatchName() {
		PatchCommon pcp = getTestParameters();
		assertTrue("Test Patch--".equals(pcp.getPatchName()));
	}

	@Test
	public void testGetPatchLevel() {
		PatchCommon pcp = getTestParameters();
		assertEquals(127, pcp.getPatchLevel().getValue());
	}

	@Test
	public void testGetPatchTempo() {
		PatchCommon pcp = getTestParameters();
		assertEquals(292, pcp.getPatchTempo().getValue());
	}

	@Test
	public void testGetArpeggioSwitch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(false, pcp.getArpeggioSwitch());
	}

	@Test
	public void testGetReserved1() {
		PatchCommon pcp = getTestParameters();
		assertEquals(1, pcp.getReserved1().getValue());
	}

	@Test
	public void testGetPortamentoSwitch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(false, pcp.getPortamentoSwitch());
	}

	@Test
	public void testGetPortamentoTime() {
		PatchCommon pcp = getTestParameters();
		assertEquals(126, pcp.getPortamentoTime().getValue());
	}

	@Test
	public void testGetMonoSwitch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(true, pcp.getMonoSwitch());
	}

	@Test
	public void testGetOctaveShift() {
		PatchCommon pcp = getTestParameters();
		assertEquals(3, pcp.getOctaveShift().getValue());
	}

	@Test
	public void testGetPitchBendRangeUp() {
		PatchCommon pcp = getTestParameters();
		assertEquals(24, pcp.getPitchBendRangeUp().getValue());
	}

	@Test
	public void testGetPitchBendRangeDown() {
		PatchCommon pcp = getTestParameters();
		assertEquals(23, pcp.getPitchBendRangeDown().getValue());
	}

	@Test
	public void testGetReserved2() {
		PatchCommon pcp = getTestParameters();
		assertEquals(0, pcp.getReserved2().getValue());
	}

	@Test
	public void testGetTone1Switch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(true, pcp.getTone1Switch());
	}

	@Test
	public void testGetTone1Select() {
		PatchCommon pcp = getTestParameters();
		assertEquals(false, pcp.getTone1Select());
	}

	@Test
	public void testGetTone2Switch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(false, pcp.getTone2Switch());
	}

	@Test
	public void testGetTone2Select() {
		PatchCommon pcp = getTestParameters();
		assertEquals(true, pcp.getTone2Select());
	}

	@Test
	public void testGetTone3Switch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(true, pcp.getTone3Switch());
	}

	@Test
	public void testGetTone3Select() {
		PatchCommon pcp = getTestParameters();
		assertEquals(false, pcp.getTone3Select());
	}

	@Test
	public void testGetSyncRingSelect() {
		PatchCommon pcp = getTestParameters();
		assertEquals(SyncRingSelect.RING, pcp.getSyncRingSelect());
	}

	@Test
	public void testGetEffectsMasterSwitch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(true, pcp.getEffectsMasterSwitch());
	}

	@Test
	public void testGetReserved3() {
		PatchCommon pcp = getTestParameters();
		assertEquals(3, pcp.getReserved3().getValue());
	}

	@Test
	public void testGetDelayTempoSyncSwitch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(false, pcp.getDelayTempoSyncSwitch());
	}

	@Test
	public void testGetLowBoostSwitch() {
		PatchCommon pcp = getTestParameters();
		assertEquals(true, pcp.getLowBoostSwitch());
	}

	@Test
	public void testGetDBeamAssign() {
		PatchCommon pcp = getTestParameters();
		assertEquals(DBeamAssign.FILTER_CUTOFF_KF, pcp.getDBeamAssign());
	}

	@Test
	public void testGetReserved4() {
		PatchCommon pcp = getTestParameters();
		assertEquals(1, pcp.getReserved4().getValue());
	}

	@Test
	public void testGetReserved5() {
		PatchCommon pcp = getTestParameters();
		assertEquals(0, pcp.getReserved5().getValue());
	}

	@Test
	public void testGetReserved6() {
		PatchCommon pcp = getTestParameters();
		assertEquals(1, pcp.getReserved6().getValue());
	}

	@Test
	public void testGetReserved7() {
		PatchCommon pcp = getTestParameters();
		assertEquals(0, pcp.getReserved7().getValue());
	}

	@Test
	public void testGetDBeamPolarity() {
		PatchCommon pcp = getTestParameters();
		assertEquals(DBeamPolarity.REVERSE, pcp.getDBeamPolarity());
	}

	@Test
	public void testGetEffectsDistortionSelect() {
		PatchCommon pcp = getTestParameters();
		assertEquals(false, pcp.getEffectsDistortionSelect());
	}

	@Test
	public void testGetEffectsFlangerSelect() {
		PatchCommon pcp = getTestParameters();
		assertEquals(true, pcp.getEffectsFlangerSelect());
	}

	@Test
	public void testGetEffectsDelaySelect() {
		PatchCommon pcp = getTestParameters();
		assertEquals(false, pcp.getEffectsDelaySelect());
	}

	@Test
	public void testGetEffectsReverbSelect() {
		PatchCommon pcp = getTestParameters();
		assertEquals(true, pcp.getEffectsReverbSelect());
	}

	@Test
	public void testGetReserved8() {
		PatchCommon pcp = getTestParameters();
		assertEquals(0, pcp.getReserved8().getValue());
	}

	@Test
	public void testGetReserved9() {
		PatchCommon pcp = getTestParameters();
		assertEquals(1, pcp.getReserved9().getValue());
	}

	@Test
	public void testGetReserved10() {
		PatchCommon pcp = getTestParameters();
		assertEquals(0, pcp.getReserved10().getValue());
	}

	@Test
	public void testGetReserved11() {
		PatchCommon pcp = getTestParameters();
		assertEquals(1, pcp.getReserved11().getValue());
	}

	@Test
	public void testGetReserved12() {
		PatchCommon pcp = getTestParameters();
		assertEquals(0, pcp.getReserved12().getValue());
	}

	@Test
	public void testGetReserved13() {
		PatchCommon pcp = getTestParameters();
		assertEquals(1, pcp.getReserved13().getValue());
	}

	@Test
	public void testGetReserved14() {
		PatchCommon pcp = getTestParameters();
		assertEquals(125, pcp.getReserved14().getValue());
	}

	@Test
	public void testGetReserved15() {
		PatchCommon pcp = getTestParameters();
		assertEquals(124, pcp.getReserved15().getValue());
	}

	@Test
	public void testGetReserved16() {
		PatchCommon pcp = getTestParameters();
		assertEquals(123, pcp.getReserved16().getValue());
	}

	@Test
	public void testGetReserved17() {
		PatchCommon pcp = getTestParameters();
		assertEquals(122, pcp.getReserved17().getValue());
	}

	@Test
	public void testGetReserved18() {
		PatchCommon pcp = getTestParameters();
		assertEquals(121, pcp.getReserved18().getValue());
	}

	@Test
	public void testGetReserved19() {
		PatchCommon pcp = getTestParameters();
		assertEquals(120, pcp.getReserved19().getValue());
	}

	@Test
	public void testGetReserved20() {
		PatchCommon pcp = getTestParameters();
		assertEquals(-1, pcp.getReserved20().getValue());
	}

	@Test
	public void testGetReserved21() {
		PatchCommon pcp = getTestParameters();
		assertEquals(-2, pcp.getReserved21().getValue());
	}

	@Test
	public void testGetReserved22() {
		PatchCommon pcp = getTestParameters();
		assertEquals(-3, pcp.getReserved22().getValue());
	}

}
