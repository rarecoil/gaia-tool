/**
 * 
 */
package nl.grauw.gaia_tool.parameters;

import static org.junit.Assert.*;

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
	
	static byte[] testAddressMap = {
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

	@Test (expected = RuntimeException.class)
	public void testSystemParameters() {
		new SystemParameters(new byte[109]);
	}

	@Test
	public void testGetBankSelectMSB() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(87, sp.getBankSelectMSB().getValue());
	}

	@Test
	public void testGetBankSelectLSB() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(64, sp.getBankSelectLSB().getValue());
	}

	@Test
	public void testGetProgramNumber() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(33, sp.getProgramNumber().getValue());
	}

	@Test
	public void testGetMasterLevel() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(127, sp.getMasterLevel().getValue());
	}

	@Test
	public void testGetMasterTune() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(18, sp.getMasterTune().getValue());
	}

	@Test
	public void testGetPatchRemain() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(true, sp.getPatchRemain());
	}

	@Test
	public void testGetClockSource() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(ClockSource.USB, sp.getClockSource());
	}

	@Test
	public void testGetSystemTempo() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(292, sp.getSystemTempo().getValue());
	}

	@Test
	public void testGetKeyboardVelocity() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(KeyboardVelocity.REAL, sp.getKeyboardVelocity());
	}

	@Test
	public void testGetPedalPolarity() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(PedalPolarity.REVERSE, sp.getPedalPolarity());
	}

	@Test
	public void testGetPedalAssign() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(PedalAssign.TAP_TEMPO, sp.getPedalAssign());
	}

	@Test
	public void testGetDBeamSens() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(7, sp.getDBeamSens().getValue());
	}

	@Test
	public void testGetRxTxChannel() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(15, sp.getRxTxChannel().getValue());
	}

	@Test
	public void testGetMidiUSBThru() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(true, sp.getMidiUSBThru());
	}

	@Test
	public void testGetSoftThru() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(false, sp.getSoftThru());
	}

	@Test
	public void testGetRxProgramChange() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(true, sp.getRxProgramChange());
	}

	@Test
	public void testGetRxBankSelect() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(false, sp.getRxBankSelect());
	}

	@Test
	public void testGetRemoteKeyboard() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(true, sp.getRemoteKeyboard());
	}

	@Test
	public void testGetTxProgramChange() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(false, sp.getTxProgramChange());
	}

	@Test
	public void testGetTxBankSelect() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(true, sp.getTxBankSelect());
	}

	@Test
	public void testGetTxEditData() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(false, sp.getTxEditData());
	}

	@Test
	public void testGetRecorderSyncOutput() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(true, sp.getRecorderSyncOutput());
	}

	@Test
	public void testGetRecorderMetronomeMode() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(RecorderMetronomeMode.REC_AND_PLAY, sp.getRecorderMetronomeMode());
	}

	@Test
	public void testGetRecorderMetronomeLevel() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(5, sp.getRecorderMetronomeLevel().getValue());
	}

	@Test
	public void testGetReserved1() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(0, sp.getReserved1().getValue());
	}

	@Test
	public void testGetReserved2() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(126, sp.getReserved2().getValue());
	}

	@Test
	public void testGetReserved3() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(125, sp.getReserved3().getValue());
	}

	@Test
	public void testGetReserved4() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(1, sp.getReserved4().getValue());
	}

	@Test
	public void testGetReserved5() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(-5, sp.getReserved5().getValue());
	}

	@Test
	public void testGetReserved6() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(-3, sp.getReserved6().getValue());
	}

	@Test
	public void testGetReserved7() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(124, sp.getReserved7().getValue());
	}

	@Test
	public void testGetReserved8() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(1, sp.getReserved8().getValue());
	}

	@Test
	public void testGetReserved9() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(0, sp.getReserved9().getValue());
	}

	@Test
	public void testGetReserved10() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(1, sp.getReserved10().getValue());
	}

	@Test
	public void testGetReserved11() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(0, sp.getReserved11().getValue());
	}

	@Test
	public void testGetReserved12() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(123, sp.getReserved12().getValue());
	}

	@Test
	public void testGetReserved13() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(122, sp.getReserved13().getValue());
	}

	@Test
	public void testGetReserved14() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(57, sp.getReserved14().getValue());
	}

	@Test
	public void testGetWriteProtect() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(true, sp.getWriteProtect(0, 0));
		assertEquals(false, sp.getWriteProtect(0, 1));
		assertEquals(false, sp.getWriteProtect(1, 0));
		assertEquals(true, sp.getWriteProtect(1, 1));
		assertEquals(true, sp.getWriteProtect(2, 6));
		assertEquals(false, sp.getWriteProtect(6, 2));
		assertEquals(false, sp.getWriteProtect(6, 5));
		assertEquals(true, sp.getWriteProtect(7, 7));
	}

	@Test
	public void testGetPowerSaveMode() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(PowerSaveMode.MIN_10, sp.getPowerSaveMode());
	}

	@Test
	public void testGetReserved15() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(12, sp.getReserved15().getValue());
	}

	@Test
	public void testGetReserved16() {
		SystemParameters sp = new SystemParameters(testAddressMap);
		assertEquals(13, sp.getReserved16().getValue());
	}

}
