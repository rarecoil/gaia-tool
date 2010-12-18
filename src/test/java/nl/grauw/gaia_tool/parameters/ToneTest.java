package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.parameters.Tone.FilterMode;
import nl.grauw.gaia_tool.parameters.Tone.FilterSlope;
import nl.grauw.gaia_tool.parameters.Tone.LFOShape;
import nl.grauw.gaia_tool.parameters.Tone.LFOTempoSyncNote;
import nl.grauw.gaia_tool.parameters.Tone.OSCWave;
import nl.grauw.gaia_tool.parameters.Tone.OSCWaveVariation;

import org.junit.Test;

public class ToneTest {
	
	static Address testAddress = new Address(0x10, 0x00, 0x01, 0x00);
	static byte[] testParameterData = {
		0x06, 0x02, 0x01, 0x58, 0x0E, 0x7F, 0x7E, 0x7D, // 0x00
		0x7C, 0x3F, 0x04, 0x01, 0x7B, 0x36, 0x3E, 0x7A, // 0x08
		0x79, 0x78, 0x77, 0x76, 0x3D, 0x75, 0x3C, 0x74, // 0x10
		0x73, 0x72, 0x71, 0x00, 0x05, 0x70, 0x01, 0x11, // 0x18
		0x6F, 0x01, 0x3B, 0x3A, 0x39, 0x38, 0x03, 0x6E, // 0x20
		0x00, 0x13, 0x6D, 0x01, 0x37, 0x36, 0x35, 0x34, // 0x28
		0x33, 0x32, 0x31, 0x30, 0x00, 0x01, 0x00, 0x01, // 0x30
		0x6C, 0x6B, 0x6A, 0x69, 0x2F, 0x2E
	};
	
	public static Tone getTestParameters() {
		return new Tone(testAddress, testParameterData);
	}

	@Test (expected = RuntimeException.class)
	public void testPatchToneParameters() {
		new Tone(testAddress, new byte[61]);
	}

	@Test
	public void testGetOSCWave() {
		Tone ptp = getTestParameters();
		assertEquals(OSCWave.SUPER_SAW, ptp.getOSCWave());
	}

	@Test
	public void testGetOSCWaveVariation() {
		Tone ptp = getTestParameters();
		assertEquals(OSCWaveVariation.C, ptp.getOSCWaveVariation());
	}

	@Test
	public void testGetReserved1() {
		Tone ptp = getTestParameters();
		assertEquals(true, ptp.getReserved1());
	}

	@Test
	public void testGetOSCPitch() {
		Tone ptp = getTestParameters();
		assertEquals(24, ptp.getOSCPitch().getValue());
	}

	@Test
	public void testGetOSCDetune() {
		Tone ptp = getTestParameters();
		assertEquals(-50, ptp.getOSCDetune().getValue());
	}

	@Test
	public void testGetOSCPulseWidthModDepth() {
		Tone ptp = getTestParameters();
		assertEquals(127, ptp.getOSCPulseWidthModDepth().getValue());
	}

	@Test
	public void testGetOSCPulseWidth() {
		Tone ptp = getTestParameters();
		assertEquals(126, ptp.getOSCPulseWidth().getValue());
	}

	@Test
	public void testGetOSCPitchEnvAttackTime() {
		Tone ptp = getTestParameters();
		assertEquals(125, ptp.getOSCPitchEnvAttackTime().getValue());
	}

	@Test
	public void testGetOSCPitchEnvDecay() {
		Tone ptp = getTestParameters();
		assertEquals(124, ptp.getOSCPitchEnvDecay().getValue());
	}

	@Test
	public void testGetOSCPitchEnvDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-1, ptp.getOSCPitchEnvDepth().getValue());
	}

	@Test
	public void testGetFilterMode() {
		Tone ptp = getTestParameters();
		assertEquals(FilterMode.PKG, ptp.getFilterMode());
	}

	@Test
	public void testGetFilterSlope() {
		Tone ptp = getTestParameters();
		assertEquals(FilterSlope.MINUS_24_DB, ptp.getFilterSlope());
	}

	@Test
	public void testGetFilterCutoff() {
		Tone ptp = getTestParameters();
		assertEquals(123, ptp.getFilterCutoff().getValue());
	}

	@Test
	public void testGetFilterCutoffKeyfollow() {
		Tone ptp = getTestParameters();
		assertEquals(-10, ptp.getFilterCutoffKeyfollow().getValue());
	}

	@Test
	public void testGetFilterEnvVelocitySens() {
		Tone ptp = getTestParameters();
		assertEquals(-2, ptp.getFilterEnvVelocitySens().getValue());
	}

	@Test
	public void testGetFilterResonance() {
		Tone ptp = getTestParameters();
		assertEquals(122, ptp.getFilterResonance().getValue());
	}

	@Test
	public void testGetFilterEnvAttackTime() {
		Tone ptp = getTestParameters();
		assertEquals(121, ptp.getFilterEnvAttackTime().getValue());
	}

	@Test
	public void testGetFilterEnvDecayTime() {
		Tone ptp = getTestParameters();
		assertEquals(120, ptp.getFilterEnvDecayTime().getValue());
	}

	@Test
	public void testGetFilterEnvSustainLevel() {
		Tone ptp = getTestParameters();
		assertEquals(119, ptp.getFilterEnvSustainLevel().getValue());
	}

	@Test
	public void testGetFilterEnvReleaseTime() {
		Tone ptp = getTestParameters();
		assertEquals(118, ptp.getFilterEnvReleaseTime().getValue());
	}

	@Test
	public void testGetFilterEnvDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-3, ptp.getFilterEnvDepth().getValue());
	}

	@Test
	public void testGetAmpLevel() {
		Tone ptp = getTestParameters();
		assertEquals(117, ptp.getAmpLevel().getValue());
	}

	@Test
	public void testGetAmpLevelVelocitySens() {
		Tone ptp = getTestParameters();
		assertEquals(-4, ptp.getAmpLevelVelocitySens().getValue());
	}

	@Test
	public void testGetAmpEnvAttackTime() {
		Tone ptp = getTestParameters();
		assertEquals(116, ptp.getAmpEnvAttackTime().getValue());
	}

	@Test
	public void testGetAmpEnvDecayTime() {
		Tone ptp = getTestParameters();
		assertEquals(115, ptp.getAmpEnvDecayTime().getValue());
	}

	@Test
	public void testGetAmpEnvSustainLevel() {
		Tone ptp = getTestParameters();
		assertEquals(114, ptp.getAmpEnvSustainLevel().getValue());
	}

	@Test
	public void testGetAmpEnvReleaseTime() {
		Tone ptp = getTestParameters();
		assertEquals(113, ptp.getAmpEnvReleaseTime().getValue());
	}

	@Test
	public void testGetAmpPan() {
		Tone ptp = getTestParameters();
		assertEquals(-64, ptp.getAmpPan().getValue());
	}

	@Test
	public void testGetLFOShape() {
		Tone ptp = getTestParameters();
		assertEquals(LFOShape.RND, ptp.getLFOShape());
	}

	@Test
	public void testGetLFORate() {
		Tone ptp = getTestParameters();
		assertEquals(112, ptp.getLFORate().getValue());
	}

	@Test
	public void testGetLFOTempoSyncSwitch() {
		Tone ptp = getTestParameters();
		assertEquals(true, ptp.getLFOTempoSyncSwitch());
	}

	@Test
	public void testGetLFOTempoSyncNote() {
		Tone ptp = getTestParameters();
		assertEquals(LFOTempoSyncNote._1_16TH, ptp.getLFOTempoSyncNote());
	}

	@Test
	public void testGetLFOFadeTime() {
		Tone ptp = getTestParameters();
		assertEquals(111, ptp.getLFOFadeTime().getValue());
	}

	@Test
	public void testGetLFOKeyTrigger() {
		Tone ptp = getTestParameters();
		assertEquals(true, ptp.getLFOKeyTrigger());
	}

	@Test
	public void testGetLFOPitchDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-5, ptp.getLFOPitchDepth().getValue());
	}

	@Test
	public void testGetLFOFilterDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-6, ptp.getLFOFilterDepth().getValue());
	}

	@Test
	public void testGetLFOAmpDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-7, ptp.getLFOAmpDepth().getValue());
	}

	@Test
	public void testGetLFOPanDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-8, ptp.getLFOPanDepth().getValue());
	}

	@Test
	public void testGetModulationLFOShape() {
		Tone ptp = getTestParameters();
		assertEquals(LFOShape.SQR, ptp.getModulationLFOShape());
	}

	@Test
	public void testGetModulationLFORate() {
		Tone ptp = getTestParameters();
		assertEquals(110, ptp.getModulationLFORate().getValue());
	}

	@Test
	public void testGetModulationLFOTempoSyncSwitch() {
		Tone ptp = getTestParameters();
		assertEquals(false, ptp.getModulationLFOTempoSyncSwitch());
	}

	@Test
	public void testGetModulationLFOTempoSyncNote() {
		Tone ptp = getTestParameters();
		assertEquals(LFOTempoSyncNote._1_32ND, ptp.getModulationLFOTempoSyncNote());
	}

	@Test
	public void testGetReserved2() {
		Tone ptp = getTestParameters();
		assertEquals(109, ptp.getReserved2().getValue());
	}

	@Test
	public void testGetReserved3() {
		Tone ptp = getTestParameters();
		assertEquals(1, ptp.getReserved3().getValue());
	}

	@Test
	public void testGetModulationLFOPitchDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-9, ptp.getModulationLFOPitchDepth().getValue());
	}

	@Test
	public void testGetModulationLFOFilterDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-10, ptp.getModulationLFOFilterDepth().getValue());
	}

	@Test
	public void testGetModulationLFOAmpDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-11, ptp.getModulationLFOAmpDepth().getValue());
	}

	@Test
	public void testGetModulationLFOPanDepth() {
		Tone ptp = getTestParameters();
		assertEquals(-12, ptp.getModulationLFOPanDepth().getValue());
	}

	@Test
	public void testGetReserved4() {
		Tone ptp = getTestParameters();
		assertEquals(-13, ptp.getReserved4().getValue());
	}

	@Test
	public void testGetReserved5() {
		Tone ptp = getTestParameters();
		assertEquals(-14, ptp.getReserved5().getValue());
	}

	@Test
	public void testGetReserved6() {
		Tone ptp = getTestParameters();
		assertEquals(-15, ptp.getReserved6().getValue());
	}

	@Test
	public void testGetReserved7() {
		Tone ptp = getTestParameters();
		assertEquals(-16, ptp.getReserved7().getValue());
	}

	@Test
	public void testGetReserved8() {
		Tone ptp = getTestParameters();
		assertEquals(0, ptp.getReserved8().getValue());
	}

	@Test
	public void testGetReserved9() {
		Tone ptp = getTestParameters();
		assertEquals(1, ptp.getReserved9().getValue());
	}

	@Test
	public void testGetReserved10() {
		Tone ptp = getTestParameters();
		assertEquals(0, ptp.getReserved10().getValue());
	}

	@Test
	public void testGetReserved11() {
		Tone ptp = getTestParameters();
		assertEquals(1, ptp.getReserved11().getValue());
	}

	@Test
	public void testGetReserved12() {
		Tone ptp = getTestParameters();
		assertEquals(108, ptp.getReserved12().getValue());
	}

	@Test
	public void testGetReserved13() {
		Tone ptp = getTestParameters();
		assertEquals(107, ptp.getReserved13().getValue());
	}

	@Test
	public void testGetReserved14() {
		Tone ptp = getTestParameters();
		assertEquals(106, ptp.getReserved14().getValue());
	}

	@Test
	public void testGetReserved15() {
		Tone ptp = getTestParameters();
		assertEquals(105, ptp.getReserved15().getValue());
	}

	@Test
	public void testGetReserved16() {
		Tone ptp = getTestParameters();
		assertEquals(-17, ptp.getReserved16().getValue());
	}

	@Test
	public void testGetReserved17() {
		Tone ptp = getTestParameters();
		assertEquals(-18, ptp.getReserved17().getValue());
	}

}
