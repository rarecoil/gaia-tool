package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import nl.grauw.gaia_tool.messages.GMSystemOff;
import nl.grauw.gaia_tool.messages.IdentityRequest;

/**
 * Represents the Roland GAIA SH-01 synthesizer
 * 
 * Before use, the object should be initialised by calling open().
 * When you are done, you must invoke close() to clean up.
 * 
 * The GAIA actually has three synthesizers:
 * - The polyphonic virtual analog synthesizer (default: channel 0)
 * - A general midi synthesizer (default: channel 1)
 * - A second general midi synthesizer (default: channel 2 and higher (!))
 * 
 * The virtual analog synth channel can actually be configured with the
 * RX/TX channel setting so this value can be changed, I’m assuming in
 * that case the 1st GM synth will move to channel 0 and the 2nd one
 * will also adjust accordingly.
 */
public class Gaia {

	MidiDevice midi_in;
	MidiDevice midi_out;
	Receiver receiver;
	Transmitter transmitter;
	ResponseReceiver responseReceiver = new ResponseReceiver();
	
	final static int synth_channel = 0;
	final static int gm_channel = 1;
	final static int gm2_channel = 2;

	public Gaia() {
	}
	
	/**
	 * Initialises the system.
	 */
	public void open() throws MidiUnavailableException {
		discoverGaiaDevices();
		midi_in.open();
		midi_out.open();
		receiver = midi_in.getReceiver();
		transmitter = midi_out.getTransmitter();
		transmitter.setReceiver(responseReceiver);
	}
	
	/**
	 * Cleans up the system.
	 */
	public void close() throws MidiUnavailableException {
		midi_in.close();
		midi_out.close();
		receiver.close();
		transmitter.close();
		responseReceiver.close();
	}
	
	private void discoverGaiaDevices() throws MidiUnavailableException {
		MidiDevice.Info[] mdi_arr = MidiSystem.getMidiDeviceInfo();
		
		for (MidiDevice.Info mdi : mdi_arr) {
			if (mdi.getName().contains("SH-01")) {
				MidiDevice md = MidiSystem.getMidiDevice(mdi);
				if (md.getMaxTransmitters() == 0) {
					midi_in = md;
					System.out.println("Found (IN): " + mdi);
				} else {
					midi_out = md;
					System.out.println("Found (OUT): " + mdi);
				}
			}
		}
	}
	
	/**
	 * Plays a C-4 note for one second.
	 */
	public void playTestNote() throws InvalidMidiDataException {
		ShortMessage program_change = new ShortMessage();
		program_change.setMessage(ShortMessage.PROGRAM_CHANGE, synth_channel, 0, 0);
		receiver.send(program_change, -1);
		
		ShortMessage note_on = new ShortMessage();
		note_on.setMessage(ShortMessage.NOTE_ON, synth_channel, 60, 127);
		receiver.send(note_on, -1);
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		ShortMessage note_off = new ShortMessage();
		note_off.setMessage(ShortMessage.NOTE_OFF, synth_channel, 60, 127);
		receiver.send(note_off, -1);
	}
	
	/**
	 * Plays a C-4 note for one second on the first GM channel.
	 */
	public void playGMTestNote() throws InvalidMidiDataException {
		receiver.send(new GMSystemOff(), -1);
		
//		SysexMessage gm_off = new SysexMessage();
//		byte[] gm_off_data = {0x7E, 0x7F, 0x09, 0x02, (byte)0xF7};
//		gm_off.setMessage(0xF0, gm_off_data, gm_off_data.length);
//		receiver.send(gm_off, -1);
		
		ShortMessage program_change = new ShortMessage();
		program_change.setMessage(ShortMessage.PROGRAM_CHANGE, gm_channel, 0, 0);
		receiver.send(program_change, -1);
		
		ShortMessage note_on = new ShortMessage();
		note_on.setMessage(ShortMessage.NOTE_ON, gm_channel, 60, 127);
		receiver.send(note_on, -1);
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		ShortMessage note_off = new ShortMessage();
		note_off.setMessage(ShortMessage.NOTE_OFF, gm_channel, 60, 127);
		receiver.send(note_off, -1);
	}
	
	/**
	 * Plays a C-4 note for one second on the second GM channel.
	 */
	public void playGM2TestNote() throws InvalidMidiDataException {
		ShortMessage program_change = new ShortMessage();
		program_change.setMessage(ShortMessage.PROGRAM_CHANGE, gm2_channel, 10, 0);
		receiver.send(program_change, -1);
		
		ShortMessage note_on = new ShortMessage();
		note_on.setMessage(ShortMessage.NOTE_ON, gm2_channel, 60, 127);
		receiver.send(note_on, -1);
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		ShortMessage note_off = new ShortMessage();
		note_off.setMessage(ShortMessage.NOTE_OFF, gm2_channel, 60, 127);
		receiver.send(note_off, -1);
	}
	
	/**
	 * Requests the device identity.
	 * @throws InvalidMidiDataException 
	 */
	public void requestIdentity() throws InvalidMidiDataException {
		receiver.send(new IdentityRequest(), -1);
	}
	
}
