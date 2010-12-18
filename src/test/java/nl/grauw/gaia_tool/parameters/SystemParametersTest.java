/**
 * 
 */
package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia_tool.Address;
import nl.grauw.gaia_tool.ParameterData;
import nl.grauw.gaia_tool.parameters.SystemParameters.ClockSource;
import nl.grauw.gaia_tool.parameters.SystemParameters.KeyboardVelocity;
import nl.grauw.gaia_tool.parameters.SystemParameters.PedalAssign;
import nl.grauw.gaia_tool.parameters.SystemParameters.PedalPolarity;
import nl.grauw.gaia_tool.parameters.SystemParameters.PowerSaveMode;
import nl.grauw.gaia_tool.parameters.SystemParameters.RecorderMetronomeMode;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class SystemParametersTest {
	
	static Address testAddress = new Address(0x01, 0x00, 0x00, 0x00);
	static byte[] testParameterData = {
		0x57, 0x40, 0x21, 0x7F, 0x00, 0x04, 0x01, 0x02, // 0x00
		0x01, 0x03, 0x01, 0x02, 0x04, 0x00, 0x01, 0x06, // 0x08
		0x07, 0x0E, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, // 0x10
		0x01, 0x00, 0x01, 0x02, 0x05, 0x00, 0x7E, 0x7D, // 0x18
		0x01, 0x3B, 0x3D, 0x7C, 0x01, 0x00, 0x01, 0x00, // 0x20
		0x7B, 0x7A, 0x79, // 0x28
		0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, // 0x2B
		0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, // 0x33
		0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, 0x00, // 0x3B
		0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x01, // 0x43
		0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, // 0x4B
		0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x01, // 0x53
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, // 0x5B
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, // 0x63
		0x04, 0x0C, 0x0D // 0x6B
	};
	
	public static SystemParameters getTestParameters() {
		ParameterData data = new ParameterData(testAddress, testParameterData);
		return new SystemParameters(data);
	}

	@Test (expected = RuntimeException.class)
	public void testSystemParameters() {
		new SystemParameters(new ParameterData(testAddress, new byte[109]));
	}

	@Test
	public void testGetBankSelectMSB() {
		SystemParameters sp = getTestParameters();
		assertEquals(87, sp.getBankSelectMSB().getValue());
	}

	@Test
	public void testGetBankSelectLSB() {
		SystemParameters sp = getTestParameters();
		assertEquals(64, sp.getBankSelectLSB().getValue());
	}

	@Test
	public void testGetProgramNumber() {
		SystemParameters sp = getTestParameters();
		assertEquals(33, sp.getProgramNumber().getValue());
	}

	@Test
	public void testGetMasterLevel() {
		SystemParameters sp = getTestParameters();
		assertEquals(127, sp.getMasterLevel().getValue());
	}

	@Test
	public void testGetMasterTune() {
		SystemParameters sp = getTestParameters();
		assertEquals(18, sp.getMasterTune().getValue());
	}

	@Test
	public void testGetPatchRemain() {
		SystemParameters sp = getTestParameters();
		assertEquals(true, sp.getPatchRemain());
	}

	@Test
	public void testGetClockSource() {
		SystemParameters sp = getTestParameters();
		assertEquals(ClockSource.USB, sp.getClockSource());
	}

	@Test
	public void testGetSystemTempo() {
		SystemParameters sp = getTestParameters();
		assertEquals(292, sp.getSystemTempo().getValue());
	}

	@Test
	public void testGetKeyboardVelocity() {
		SystemParameters sp = getTestParameters();
		assertEquals(KeyboardVelocity.REAL, sp.getKeyboardVelocity());
	}

	@Test
	public void testGetPedalPolarity() {
		SystemParameters sp = getTestParameters();
		assertEquals(PedalPolarity.REVERSE, sp.getPedalPolarity());
	}

	@Test
	public void testGetPedalAssign() {
		SystemParameters sp = getTestParameters();
		assertEquals(PedalAssign.TAP_TEMPO, sp.getPedalAssign());
	}

	@Test
	public void testGetDBeamSens() {
		SystemParameters sp = getTestParameters();
		assertEquals(7, sp.getDBeamSens().getValue());
	}

	@Test
	public void testGetRxTxChannel() {
		SystemParameters sp = getTestParameters();
		assertEquals(15, sp.getRxTxChannel().getValue());
	}

	@Test
	public void testGetMidiUSBThru() {
		SystemParameters sp = getTestParameters();
		assertEquals(true, sp.getMidiUSBThru());
	}

	@Test
	public void testGetSoftThru() {
		SystemParameters sp = getTestParameters();
		assertEquals(false, sp.getSoftThru());
	}

	@Test
	public void testGetRxProgramChange() {
		SystemParameters sp = getTestParameters();
		assertEquals(true, sp.getRxProgramChange());
	}

	@Test
	public void testGetRxBankSelect() {
		SystemParameters sp = getTestParameters();
		assertEquals(false, sp.getRxBankSelect());
	}

	@Test
	public void testGetRemoteKeyboard() {
		SystemParameters sp = getTestParameters();
		assertEquals(true, sp.getRemoteKeyboard());
	}

	@Test
	public void testGetTxProgramChange() {
		SystemParameters sp = getTestParameters();
		assertEquals(false, sp.getTxProgramChange());
	}

	@Test
	public void testGetTxBankSelect() {
		SystemParameters sp = getTestParameters();
		assertEquals(true, sp.getTxBankSelect());
	}

	@Test
	public void testGetTxEditData() {
		SystemParameters sp = getTestParameters();
		assertEquals(false, sp.getTxEditData());
	}

	@Test
	public void testGetRecorderSyncOutput() {
		SystemParameters sp = getTestParameters();
		assertEquals(true, sp.getRecorderSyncOutput());
	}

	@Test
	public void testGetRecorderMetronomeMode() {
		SystemParameters sp = getTestParameters();
		assertEquals(RecorderMetronomeMode.REC_AND_PLAY, sp.getRecorderMetronomeMode());
	}

	@Test
	public void testGetRecorderMetronomeLevel() {
		SystemParameters sp = getTestParameters();
		assertEquals(5, sp.getRecorderMetronomeLevel().getValue());
	}

	@Test
	public void testGetReserved1() {
		SystemParameters sp = getTestParameters();
		assertEquals(0, sp.getReserved1().getValue());
	}

	@Test
	public void testGetReserved2() {
		SystemParameters sp = getTestParameters();
		assertEquals(126, sp.getReserved2().getValue());
	}

	@Test
	public void testGetReserved3() {
		SystemParameters sp = getTestParameters();
		assertEquals(125, sp.getReserved3().getValue());
	}

	@Test
	public void testGetReserved4() {
		SystemParameters sp = getTestParameters();
		assertEquals(1, sp.getReserved4().getValue());
	}

	@Test
	public void testGetReserved5() {
		SystemParameters sp = getTestParameters();
		assertEquals(-5, sp.getReserved5().getValue());
	}

	@Test
	public void testGetReserved6() {
		SystemParameters sp = getTestParameters();
		assertEquals(-3, sp.getReserved6().getValue());
	}

	@Test
	public void testGetReserved7() {
		SystemParameters sp = getTestParameters();
		assertEquals(124, sp.getReserved7().getValue());
	}

	@Test
	public void testGetReserved8() {
		SystemParameters sp = getTestParameters();
		assertEquals(1, sp.getReserved8().getValue());
	}

	@Test
	public void testGetReserved9() {
		SystemParameters sp = getTestParameters();
		assertEquals(0, sp.getReserved9().getValue());
	}

	@Test
	public void testGetReserved10() {
		SystemParameters sp = getTestParameters();
		assertEquals(1, sp.getReserved10().getValue());
	}

	@Test
	public void testGetReserved11() {
		SystemParameters sp = getTestParameters();
		assertEquals(0, sp.getReserved11().getValue());
	}

	@Test
	public void testGetReserved12() {
		SystemParameters sp = getTestParameters();
		assertEquals(123, sp.getReserved12().getValue());
	}

	@Test
	public void testGetReserved13() {
		SystemParameters sp = getTestParameters();
		assertEquals(122, sp.getReserved13().getValue());
	}

	@Test
	public void testGetReserved14() {
		SystemParameters sp = getTestParameters();
		assertEquals(57, sp.getReserved14().getValue());
	}

	@Test
	public void testGetWriteProtect() {
		SystemParameters sp = getTestParameters();
		assertEquals(true, sp.getWriteProtect(0, 0));
		assertEquals(false, sp.getWriteProtect(0, 1));
		assertEquals(false, sp.getWriteProtect(1, 0));
		assertEquals(true, sp.getWriteProtect(1, 1));
		assertEquals(true, sp.getWriteProtect(2, 6));
		assertEquals(false, sp.getWriteProtect(6, 2));
		assertEquals(false, sp.getWriteProtect(6, 5));
		assertEquals(true, sp.getWriteProtect(7, 7));
	}

	@Test (expected = RuntimeException.class)
	public void testGetWriteProtect_BankTooLow() {
		SystemParameters sp = getTestParameters();
		sp.getWriteProtect(-1, 0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetWriteProtect_BankTooHigh() {
		SystemParameters sp = getTestParameters();
		sp.getWriteProtect(8, 0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetWriteProtect_PatchTooLow() {
		SystemParameters sp = getTestParameters();
		sp.getWriteProtect(0, -1);
	}

	@Test (expected = RuntimeException.class)
	public void testGetWriteProtect_PatchTooHigh() {
		SystemParameters sp = getTestParameters();
		sp.getWriteProtect(0, 8);
	}

	@Test
	public void testGetPowerSaveMode() {
		SystemParameters sp = getTestParameters();
		assertEquals(PowerSaveMode.MIN_10, sp.getPowerSaveMode());
	}

	@Test
	public void testGetReserved15() {
		SystemParameters sp = getTestParameters();
		assertEquals(12, sp.getReserved15().getValue());
	}

	@Test
	public void testGetReserved16() {
		SystemParameters sp = getTestParameters();
		assertEquals(13, sp.getReserved16().getValue());
	}

}
