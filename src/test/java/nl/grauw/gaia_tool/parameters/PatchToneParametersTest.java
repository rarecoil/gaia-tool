package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.parameters.PatchToneParameters.FilterMode;
import nl.grauw.gaia_tool.parameters.PatchToneParameters.FilterSlope;
import nl.grauw.gaia_tool.parameters.PatchToneParameters.LFOShape;
import nl.grauw.gaia_tool.parameters.PatchToneParameters.LFOTempoSyncNote;
import nl.grauw.gaia_tool.parameters.PatchToneParameters.OSCWave;
import nl.grauw.gaia_tool.parameters.PatchToneParameters.OSCWaveVariation;

import org.junit.Test;

public class PatchToneParametersTest {
	
	static byte[] testAddressMap = {
		0x06, 0x02, 0x01, 0x58, 0x0E, 0x7F, 0x7E, 0x7D, // 0x00
		0x7C, 0x3F, 0x04, 0x01, 0x7B, 0x36, 0x3E, 0x7A, // 0x08
		0x79, 0x78, 0x77, 0x76, 0x3D, 0x75, 0x3C, 0x74, // 0x10
		0x73, 0x72, 0x71, 0x00, 0x05, 0x70, 0x01, 0x11, // 0x18
		0x6F, 0x01, 0x3B, 0x3A, 0x39, 0x38, 0x03, 0x6E, // 0x20
		0x00, 0x13, 0x6D, 0x01, 0x37, 0x36, 0x35, 0x34, // 0x28
		0x33, 0x32, 0x31, 0x30, 0x00, 0x01, 0x00, 0x01, // 0x30
		0x6C, 0x6B, 0x6A, 0x69, 0x2F, 0x2E
	};

	@Test (expected = RuntimeException.class)
	public void testPatchToneParameters() {
		new PatchToneParameters(new byte[61]);
	}

	@Test
	public void testGetOSCWave() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(OSCWave.SUPER_SAW, ptp.getOSCWave());
	}

	@Test
	public void testGetOSCWaveVariation() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(OSCWaveVariation.C, ptp.getOSCWaveVariation());
	}

	@Test
	public void testGetReserved1() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(true, ptp.getReserved1());
	}

	@Test
	public void testGetOSCPitch() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(24, ptp.getOSCPitch().getValue());
	}

	@Test
	public void testGetOSCDetune() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-50, ptp.getOSCDetune().getValue());
	}

	@Test
	public void testGetOSCPulseWidthModDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(127, ptp.getOSCPulseWidthModDepth().getValue());
	}

	@Test
	public void testGetOSCPulseWidth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(126, ptp.getOSCPulseWidth().getValue());
	}

	@Test
	public void testGetOSCPitchEnvAttackTime() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(125, ptp.getOSCPitchEnvAttackTime().getValue());
	}

	@Test
	public void testGetOSCPitchEnvDecay() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(124, ptp.getOSCPitchEnvDecay().getValue());
	}

	@Test
	public void testGetOSCPitchEnvDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-1, ptp.getOSCPitchEnvDepth().getValue());
	}

	@Test
	public void testGetFilterMode() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(FilterMode.PKG, ptp.getFilterMode());
	}

	@Test
	public void testGetFilterSlope() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(FilterSlope.MINUS_24_DB, ptp.getFilterSlope());
	}

	@Test
	public void testGetFilterCutoff() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(123, ptp.getFilterCutoff().getValue());
	}

	@Test
	public void testGetFilterCutoffKeyfollow() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-10, ptp.getFilterCutoffKeyfollow().getValue());
	}

	@Test
	public void testGetFilterEnvVelocitySens() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-2, ptp.getFilterEnvVelocitySens().getValue());
	}

	@Test
	public void testGetFilterResonance() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(122, ptp.getFilterResonance().getValue());
	}

	@Test
	public void testGetFilterEnvAttackTime() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(121, ptp.getFilterEnvAttackTime().getValue());
	}

	@Test
	public void testGetFilterEnvDecayTime() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(120, ptp.getFilterEnvDecayTime().getValue());
	}

	@Test
	public void testGetFilterEnvSustainLevel() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(119, ptp.getFilterEnvSustainLevel().getValue());
	}

	@Test
	public void testGetFilterEnvReleaseTime() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(118, ptp.getFilterEnvReleaseTime().getValue());
	}

	@Test
	public void testGetFilterEnvDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-3, ptp.getFilterEnvDepth().getValue());
	}

	@Test
	public void testGetAmpLevel() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(117, ptp.getAmpLevel().getValue());
	}

	@Test
	public void testGetAmpLevelVelocitySens() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-4, ptp.getAmpLevelVelocitySens().getValue());
	}

	@Test
	public void testGetAmpEnvAttackTime() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(116, ptp.getAmpEnvAttackTime().getValue());
	}

	@Test
	public void testGetAmpEnvDecayTime() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(115, ptp.getAmpEnvDecayTime().getValue());
	}

	@Test
	public void testGetAmpEnvSustainLevel() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(114, ptp.getAmpEnvSustainLevel().getValue());
	}

	@Test
	public void testGetAmpEnvReleaseTime() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(113, ptp.getAmpEnvReleaseTime().getValue());
	}

	@Test
	public void testGetAmpPan() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-64, ptp.getAmpPan().getValue());
	}

	@Test
	public void testGetLFOShape() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(LFOShape.RND, ptp.getLFOShape());
	}

	@Test
	public void testGetLFORate() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(112, ptp.getLFORate().getValue());
	}

	@Test
	public void testGetLFOTempoSyncSwitch() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(true, ptp.getLFOTempoSyncSwitch());
	}

	@Test
	public void testGetLFOTempoSyncNote() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(LFOTempoSyncNote._1_16TH, ptp.getLFOTempoSyncNote());
	}

	@Test
	public void testGetLFOFadeTime() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(111, ptp.getLFOFadeTime().getValue());
	}

	@Test
	public void testGetLFOKeyTrigger() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(true, ptp.getLFOKeyTrigger());
	}

	@Test
	public void testGetLFOPitchDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-5, ptp.getLFOPitchDepth().getValue());
	}

	@Test
	public void testGetLFOFilterDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-6, ptp.getLFOFilterDepth().getValue());
	}

	@Test
	public void testGetLFOAmpDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-7, ptp.getLFOAmpDepth().getValue());
	}

	@Test
	public void testGetLFOPanDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-8, ptp.getLFOPanDepth().getValue());
	}

	@Test
	public void testGetModulationLFOShape() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(LFOShape.SQR, ptp.getModulationLFOShape());
	}

	@Test
	public void testGetModulationLFORate() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(110, ptp.getModulationLFORate().getValue());
	}

	@Test
	public void testGetModulationLFOTempoSyncSwitch() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(false, ptp.getModulationLFOTempoSyncSwitch());
	}

	@Test
	public void testGetModulationLFOTempoSyncNote() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(LFOTempoSyncNote._1_32ND, ptp.getModulationLFOTempoSyncNote());
	}

	@Test
	public void testGetReserved2() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(109, ptp.getReserved2().getValue());
	}

	@Test
	public void testGetReserved3() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(1, ptp.getReserved3().getValue());
	}

	@Test
	public void testGetModulationLFOPitchDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-9, ptp.getModulationLFOPitchDepth().getValue());
	}

	@Test
	public void testGetModulationLFOFilterDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-10, ptp.getModulationLFOFilterDepth().getValue());
	}

	@Test
	public void testGetModulationLFOAmpDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-11, ptp.getModulationLFOAmpDepth().getValue());
	}

	@Test
	public void testGetModulationLFOPanDepth() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-12, ptp.getModulationLFOPanDepth().getValue());
	}

	@Test
	public void testGetReserved4() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-13, ptp.getReserved4().getValue());
	}

	@Test
	public void testGetReserved5() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-14, ptp.getReserved5().getValue());
	}

	@Test
	public void testGetReserved6() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-15, ptp.getReserved6().getValue());
	}

	@Test
	public void testGetReserved7() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-16, ptp.getReserved7().getValue());
	}

	@Test
	public void testGetReserved8() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(0, ptp.getReserved8().getValue());
	}

	@Test
	public void testGetReserved9() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(1, ptp.getReserved9().getValue());
	}

	@Test
	public void testGetReserved10() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(0, ptp.getReserved10().getValue());
	}

	@Test
	public void testGetReserved11() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(1, ptp.getReserved11().getValue());
	}

	@Test
	public void testGetReserved12() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(108, ptp.getReserved12().getValue());
	}

	@Test
	public void testGetReserved13() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(107, ptp.getReserved13().getValue());
	}

	@Test
	public void testGetReserved14() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(106, ptp.getReserved14().getValue());
	}

	@Test
	public void testGetReserved15() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(105, ptp.getReserved15().getValue());
	}

	@Test
	public void testGetReserved16() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-17, ptp.getReserved16().getValue());
	}

	@Test
	public void testGetReserved17() {
		PatchToneParameters ptp = new PatchToneParameters(testAddressMap);
		assertEquals(-18, ptp.getReserved17().getValue());
	}

}
