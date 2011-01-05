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
		BANK_SELECT(0), MODULATION(1), PORTAMENTO_TIME(5), VOLUME(7), PANPOT(10), EXPRESSION(11),
		BANK_SELECT_LSB(32), HOLD_1(64), BEND_MODE(90),
		
		// effect controls
		REVERB_CONTROL_1(12), DELAY_CONTROL_1(13), FLANGER_CONTROL_1(14), DISTORTION_CONTROL_1(15),
		REVERB_LEVEL(91), DELAY_LEVEL(92), FLANGER_LEVEL(93), DISTORTION_LEVEL(94),
		
		// tone controls
		TONE_1_LFO_RATE(16), TONE_2_LFO_RATE(17), TONE_3_LFO_RATE(18),
		TONE_1_LFO_FADE_TIME(19), TONE_2_LFO_FADE_TIME(20), TONE_3_LFO_FADE_TIME(21),
		TONE_1_LFO_PITCH_DEPTH(22), TONE_2_LFO_PITCH_DEPTH(23), TONE_3_LFO_PITCH_DEPTH(24),
		TONE_1_LFO_FILTER_DEPTH(25), TONE_2_LFO_FILTER_DEPTH(26), TONE_3_LFO_FILTER_DEPTH(27),
		TONE_1_LFO_AMP_DEPTH(28), TONE_2_LFO_AMP_DEPTH(29), TONE_3_LFO_AMP_DEPTH(30),
		TONE_1_OSC_PITCH(70), TONE_2_OSC_PITCH(71), TONE_3_OSC_PITCH(72),
		TONE_1_OSC_DETUNE(73), TONE_2_OSC_DETUNE(74), TONE_3_OSC_DETUNE(75),
		TONE_1_OSC_PULSE_WIDTH_MODULATION(76), TONE_2_OSC_PULSE_WIDTH_MODULATION(77), TONE_3_OSC_PULSE_WIDTH_MODULATION(78),
		TONE_1_OSC_PULSE_WIDTH(79), TONE_2_OSC_PULSE_WIDTH(80), TONE_3_OSC_PULSE_WIDTH(81),
		TONE_1_OSC_ENV_DEPTH(85), TONE_2_OSC_ENV_DEPTH(86), TONE_3_OSC_ENV_DEPTH(87),
		TONE_1_FILTER_CUTOFF(102), TONE_2_FILTER_CUTOFF(103), TONE_3_FILTER_CUTOFF(104),
		TONE_1_FILTER_RESONANCE(105), TONE_2_FILTER_RESONANCE(106), TONE_3_FILTER_RESONANCE(107),
		TONE_1_FILTER_ENV_DEPTH(108), TONE_2_FILTER_ENV_DEPTH(109), TONE_3_FILTER_ENV_DEPTH(110),
		TONE_1_FILTER_KEY_FOLLOW(111), TONE_2_FILTER_KEY_FOLLOW(112), TONE_3_FILTER_KEY_FOLLOW(113),
		TONE_1_AMP_LEVEL(114), TONE_2_AMP_LEVEL(115), TONE_3_AMP_LEVEL(116),
		
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
