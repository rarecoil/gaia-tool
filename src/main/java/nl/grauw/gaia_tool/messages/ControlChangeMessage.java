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
package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class ControlChangeMessage extends ShortMessage {
	
	public enum Controller {
		BANK_SELECT(0), MODULATION(1), PORTAMENTO_TIME(5), VOLUME(7), PANPOT(10),
		EXPRESSION(11), EFFECTS_REVERB_CONTROL_1(12), EFFECTS_DELAY_CONTROL_1(13),
		EFFECTS_FLANGER_CONTROL_1(14), EFFECTS_DISTORTION_CONTROL_1(15), LFO_RATE(16),
		LFO_FADE_TIME(19), LFO_PITCH_DEPTH(22), LFO_FILTER_DEPTH(25), LFO_AMP_DEPTH(28),
		BANK_SELECT_LSB(32), HOLD_1(64), OSC_PITCH(70), OSC_DETUNE(73),
		OSC_PULSE_WIDTH_MODULATION(76), OSC_PULSE_WIDTH(79), OSC_ENV_DEPTH(85),
		BEND_MODE(90), EFFECTS_REVERB_LEVEL(91), EFFECTS_DELAY_LEVEL(92),
		EFFECTS_FLANGER_LEVEL(93), EFFECTS_DISTORTION_LEVEL(94), FILTER_CUTOFF(102),
		FILTER_RESONANCE(105), FILTER_ENV_DEPTH(108), FILTER_KEY_FOLLOW(111), AMP_LEVEL(114),
		// Channel mode messages
		ALL_SOUNDS_OFF(120), RESET_ALL_CONTROLLERS(121), ALL_NOTES_OFF(123),
		OMNI_OFF(124), OMNI_ON(125), MONO(126), POLY(127);
		
		int code;
		
		Controller(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}
		
		static Controller[] controllers = new Controller[128];
		
		static {
			for (Controller c : Controller.values()) {
				controllers[c.getCode()] = c;
			}
		}
		
		/**
		 * Gets an enumeration constant by MIDI controller code.
		 * @param code The MIDI controller code to look up.
		 * @return An enumeration constant, or null if there is none for
		 *         the specified code.
		 */
		public static Controller getControllerByCode(int code) {
			return controllers[code];
		}
	}
	
	public ControlChangeMessage(ShortMessage sm) {
		super(sm.getMessage());
	}
	
	public ControlChangeMessage(int channel, Controller controller, int value) throws InvalidMidiDataException {
		this(channel, controller.getCode(), value);
	}
	
	public ControlChangeMessage(int channel, int controller, int value) throws InvalidMidiDataException {
		super();
		setMessage(CONTROL_CHANGE, channel, controller, value);
	}
	
	/**
	 * Returns the controller.
	 * @return A Controller enumeration constant, or null if there is none for
	 *         the specified code (get the code through getControllerCode()).
	 */
	public Controller getController() {
		return Controller.getControllerByCode(getData1());
	}
	
	public int getControllerNumber() {
		return getData1();
	}
	
	public int getValue() {
		return getData2();
	}
	
	public String toString() {
		Controller c = getController();
		return "Control change message. Channel: " + (getChannel() + 1) +
				". Controller: " + (c != null ? c : getControllerNumber()) +
				". Value: " + getValue();
	}

}
