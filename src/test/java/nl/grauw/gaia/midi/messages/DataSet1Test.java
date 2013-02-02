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
package nl.grauw.gaia.midi.messages;

import static org.junit.Assert.*;

import nl.grauw.gaia.Address;
import nl.grauw.gaia.midi.messages.DataSet1;
import nl.grauw.gaia.midi.messages.Message;

import org.junit.Test;

public class DataSet1Test {

	@Test
	public void testDataSet1SysexMessage() {
		byte[] semData = {(byte)0xF0, 0x41, 0x7F, 0x00, 0x00, 0x41, 0x12,
				0x01, 0x23, 0x45, 0x67, 0x76, 0x54, 0x32, 0x10, 0x24, (byte)0xF7};
		Message mm = new DataSet1(semData);
		
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x41, 0x7F, 0x00, 0x00, 0x41, 0x12,
				0x01, 0x23, 0x45, 0x67, 0x76, 0x54, 0x32, 0x10, 0x24, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

	@Test
	public void testDataSet1AddressByteArray() {
		byte[] data = { 0x76, 0x54, 0x32, 0x10 };
		Message mm = new DataSet1(new Address(0x01, 0x23, 0x45, 0x67), data);
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x41, 0x7F, 0x00, 0x00, 0x41, 0x12,
				0x01, 0x23, 0x45, 0x67, 0x76, 0x54, 0x32, 0x10, 0x24, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

	@Test
	public void testDataSet1IntAddressByteArray() {
		byte[] data = { 0x76, 0x54, 0x32, 0x10 };
		Message mm = new DataSet1(0x10, new Address(0x01, 0x23, 0x45, 0x67), data);
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x41, 0x10, 0x00, 0x00, 0x41, 0x12,
				0x01, 0x23, 0x45, 0x67, 0x76, 0x54, 0x32, 0x10, 0x24, (byte)0xF7};
		assertArrayEquals(expected, message);
	}
	
	@Test
	public void testGetAddress() {
		byte[] data = { 0x76, 0x54, 0x32, 0x10 };
		DataSet1 mm = new DataSet1(new Address(0x01, 0x23, 0x45, 0x67), data);
		assertEquals("01 23 45 67", mm.getAddress().toHexString());
	}
	
	@Test
	public void testGetDataSet() {
		byte[] data = { 0x76, 0x54, 0x32, 0x10 };
		DataSet1 mm = new DataSet1(new Address(0x01, 0x23, 0x45, 0x67), data);
		assertArrayEquals(data, mm.getDataSet());
	}

}
