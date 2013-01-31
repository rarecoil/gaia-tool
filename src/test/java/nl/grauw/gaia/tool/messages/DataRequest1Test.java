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
package nl.grauw.gaia.tool.messages;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;

import nl.grauw.gaia.tool.Address;
import nl.grauw.gaia.tool.messages.DataRequest1;

import org.junit.Test;

public class DataRequest1Test {

	@Test
	public void testDataRequest1AddressInt() throws InvalidMidiDataException {
		Message mm = new DataRequest1(new Address(0x01, 0x23, 0x45, 0x67), 0x17F);
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x41, 0x7F, 0x00, 0x00, 0x41, 0x11,
				0x01, 0x23, 0x45, 0x67, 0x00, 0x00, 0x02, 0x7F, 0x2F, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

	@Test
	public void testDataRequest1IntAddressInt() throws InvalidMidiDataException {
		Message mm = new DataRequest1(0x10, new Address(0x01, 0x23, 0x45, 0x67), 0x17F);
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x41, 0x10, 0x00, 0x00, 0x41, 0x11,
				0x01, 0x23, 0x45, 0x67, 0x00, 0x00, 0x02, 0x7F, 0x2F, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

}
