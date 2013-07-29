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

public class ChannelMessage extends Message {
	
	public static final int NOTE_OFF = 0x80;
	public static final int NOTE_ON = 0x90;
	public static final int POLY_PRESSURE = 0xA0;
	public static final int CONTROL_CHANGE = 0xB0;
	public static final int PROGRAM_CHANGE = 0xC0;
	public static final int CHANNEL_PRESSURE = 0xD0;
	public static final int PITCH_BEND = 0xE0;
	
	protected ChannelMessage(byte[] message) {
		super(message);
	}
	
	protected ChannelMessage(int status, int channel, int data1) {
		super(new byte[] { (byte)(status | channel), (byte)data1 });
		
		if (channel < 0 || channel > 15)
			throw new IllegalArgumentException("Channel out of range.");
		if (status != PROGRAM_CHANGE && status != CHANNEL_PRESSURE)
			throw new IllegalArgumentException("Invalid status.");
	}
	
	protected ChannelMessage(int status, int channel, int data1, int data2) {
		super(new byte[] { (byte)(status | channel), (byte)data1, (byte)data2 });
		
		if (channel < 0 || channel > 15)
			throw new IllegalArgumentException("Channel out of range.");
		if (status != NOTE_OFF && status != NOTE_ON && status != POLY_PRESSURE && status != CONTROL_CHANGE && status != PITCH_BEND)
			throw new IllegalArgumentException("Invalid status.");
	}
	
	@Override
	public int getStatus() {
		return super.getStatus() & 0xF0;
	}
	
	public int getChannel() {
		return super.getStatus() & 0x0F;
	}
	
}
