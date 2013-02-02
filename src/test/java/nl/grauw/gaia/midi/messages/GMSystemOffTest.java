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

import nl.grauw.gaia.midi.messages.GMSystemOff;
import nl.grauw.gaia.midi.messages.Message;

import org.junit.Test;

public class GMSystemOffTest {

	@Test
	public void testGMSystemOff() {
		Message mm = new GMSystemOff();
		byte[] message = mm.getMessage();
		byte[] expected = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x02, (byte)0xF7};
		assertArrayEquals(expected, message);
	}

}
