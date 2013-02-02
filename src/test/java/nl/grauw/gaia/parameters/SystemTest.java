/*
 * Copyright 2010 Laurens Holst
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.grauw.gaia.parameters;

import static org.junit.Assert.*;

import nl.grauw.gaia.Address;
import nl.grauw.gaia.parameters.System;
import nl.grauw.gaia.parameters.System.ClockSource;
import nl.grauw.gaia.parameters.System.KeyboardVelocity;
import nl.grauw.gaia.parameters.System.PedalAssign;
import nl.grauw.gaia.parameters.System.PedalPolarity;
import nl.grauw.gaia.parameters.System.PowerSaveMode;
import nl.grauw.gaia.parameters.System.RecorderMetronomeMode;

import org.junit.Test;

public class SystemTest {
	
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
	
	public static System createTestParameters() {
		return new System(testAddress, testParameterData.clone());
	}

	@Test (expected = RuntimeException.class)
	public void testSystemParameters() {
		new System(testAddress, new byte[109]);
	}

	@Test
	public void testGetBankSelect() {
		System sp = createTestParameters();
		assertEquals(11200, sp.getBankSelect().getValue());
	}

	@Test
	public void testGetBankSelectMSB() {
		System sp = createTestParameters();
		assertEquals(87, sp.getBankSelectMSB().getValue());
	}

	@Test
	public void testGetBankSelectLSB() {
		System sp = createTestParameters();
		assertEquals(64, sp.getBankSelectLSB().getValue());
	}

	@Test
	public void testGetProgramNumber() {
		System sp = createTestParameters();
		assertEquals(33, sp.getProgramNumber().getValue());
	}

	@Test
	public void testGetMasterLevel() {
		System sp = createTestParameters();
		assertEquals(127, sp.getMasterLevel().getValue());
	}

	@Test
	public void testGetMasterTune() {
		System sp = createTestParameters();
		assertEquals(18, sp.getMasterTune().getValue());
	}

	@Test
	public void testGetPatchRemain() {
		System sp = createTestParameters();
		assertEquals(true, sp.getPatchRemain());
	}

	@Test
	public void testGetClockSource() {
		System sp = createTestParameters();
		assertEquals(ClockSource.USB, sp.getClockSource());
	}

	@Test
	public void testGetSystemTempo() {
		System sp = createTestParameters();
		assertEquals(292, sp.getSystemTempo().getValue());
	}

	@Test
	public void testGetKeyboardVelocity() {
		System sp = createTestParameters();
		assertEquals(KeyboardVelocity.REAL, sp.getKeyboardVelocity());
	}

	@Test
	public void testGetPedalPolarity() {
		System sp = createTestParameters();
		assertEquals(PedalPolarity.REVERSE, sp.getPedalPolarity());
	}

	@Test
	public void testGetPedalAssign() {
		System sp = createTestParameters();
		assertEquals(PedalAssign.TAP_TEMPO, sp.getPedalAssign());
	}

	@Test
	public void testGetDBeamSens() {
		System sp = createTestParameters();
		assertEquals(7, sp.getDBeamSens().getValue());
	}

	@Test
	public void testGetRxTxChannel() {
		System sp = createTestParameters();
		assertEquals(15, sp.getRxTxChannel().getValue());
	}

	@Test
	public void testGetMidiUSBThru() {
		System sp = createTestParameters();
		assertEquals(true, sp.getMidiUSBThru());
	}

	@Test
	public void testGetSoftThru() {
		System sp = createTestParameters();
		assertEquals(false, sp.getSoftThru());
	}

	@Test
	public void testGetRxProgramChange() {
		System sp = createTestParameters();
		assertEquals(true, sp.getRxProgramChange());
	}

	@Test
	public void testGetRxBankSelect() {
		System sp = createTestParameters();
		assertEquals(false, sp.getRxBankSelect());
	}

	@Test
	public void testGetRemoteKeyboard() {
		System sp = createTestParameters();
		assertEquals(true, sp.getRemoteKeyboard());
	}

	@Test
	public void testGetTxProgramChange() {
		System sp = createTestParameters();
		assertEquals(false, sp.getTxProgramChange());
	}

	@Test
	public void testGetTxBankSelect() {
		System sp = createTestParameters();
		assertEquals(true, sp.getTxBankSelect());
	}

	@Test
	public void testGetTxEditData() {
		System sp = createTestParameters();
		assertEquals(false, sp.getTxEditData());
	}

	@Test
	public void testGetRecorderSyncOutput() {
		System sp = createTestParameters();
		assertEquals(true, sp.getRecorderSyncOutput());
	}

	@Test
	public void testGetRecorderMetronomeMode() {
		System sp = createTestParameters();
		assertEquals(RecorderMetronomeMode.REC_AND_PLAY, sp.getRecorderMetronomeMode());
	}

	@Test
	public void testGetRecorderMetronomeLevel() {
		System sp = createTestParameters();
		assertEquals(5, sp.getRecorderMetronomeLevel().getValue());
	}

	@Test
	public void testGetReserved1() {
		System sp = createTestParameters();
		assertEquals(0, sp.getReserved1().getValue());
	}

	@Test
	public void testGetReserved2() {
		System sp = createTestParameters();
		assertEquals(126, sp.getReserved2().getValue());
	}

	@Test
	public void testGetReserved3() {
		System sp = createTestParameters();
		assertEquals(125, sp.getReserved3().getValue());
	}

	@Test
	public void testGetReserved4() {
		System sp = createTestParameters();
		assertEquals(1, sp.getReserved4().getValue());
	}

	@Test
	public void testGetReserved5() {
		System sp = createTestParameters();
		assertEquals(-5, sp.getReserved5().getValue());
	}

	@Test
	public void testGetReserved6() {
		System sp = createTestParameters();
		assertEquals(-3, sp.getReserved6().getValue());
	}

	@Test
	public void testGetReserved7() {
		System sp = createTestParameters();
		assertEquals(124, sp.getReserved7().getValue());
	}

	@Test
	public void testGetReserved8() {
		System sp = createTestParameters();
		assertEquals(1, sp.getReserved8().getValue());
	}

	@Test
	public void testGetReserved9() {
		System sp = createTestParameters();
		assertEquals(0, sp.getReserved9().getValue());
	}

	@Test
	public void testGetGMPartsSwitch() {
		System sp = createTestParameters();
		assertEquals(true, sp.getGMPartsSwitch());
	}

	@Test
	public void testGetReserved11() {
		System sp = createTestParameters();
		assertEquals(0, sp.getReserved11().getValue());
	}

	@Test
	public void testGetReserved12() {
		System sp = createTestParameters();
		assertEquals(123, sp.getReserved12().getValue());
	}

	@Test
	public void testGetReserved13() {
		System sp = createTestParameters();
		assertEquals(122, sp.getReserved13().getValue());
	}

	@Test
	public void testGetReserved14() {
		System sp = createTestParameters();
		assertEquals(57, sp.getReserved14().getValue());
	}

	@Test
	public void testGetWriteProtect() {
		System sp = createTestParameters();
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
		System sp = createTestParameters();
		sp.getWriteProtect(-1, 0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetWriteProtect_BankTooHigh() {
		System sp = createTestParameters();
		sp.getWriteProtect(8, 0);
	}

	@Test (expected = RuntimeException.class)
	public void testGetWriteProtect_PatchTooLow() {
		System sp = createTestParameters();
		sp.getWriteProtect(0, -1);
	}

	@Test (expected = RuntimeException.class)
	public void testGetWriteProtect_PatchTooHigh() {
		System sp = createTestParameters();
		sp.getWriteProtect(0, 8);
	}

	@Test
	public void testGetPowerSaveMode() {
		System sp = createTestParameters();
		assertEquals(PowerSaveMode.MIN_10, sp.getPowerSaveMode());
	}

	@Test
	public void testGetReserved15() {
		System sp = createTestParameters();
		assertEquals(12, sp.getReserved15().getValue());
	}

	@Test
	public void testGetReserved16() {
		System sp = createTestParameters();
		assertEquals(13, sp.getReserved16().getValue());
	}

}
