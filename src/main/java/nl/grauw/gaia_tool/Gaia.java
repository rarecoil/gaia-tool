package nl.grauw.gaia_tool;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

/**
 * Represents the Roland Gaia SH-01 synthesizer
 * 
 * Before use, the object should be initialised by calling open().
 * When you are done, you must invoke close() to clean up.
 */
public class Gaia {

	MidiDevice midi_in;
	MidiDevice midi_out;
	Receiver receiver;
	Transmitter transmitter;
	ResponseReceiver responseReceiver = new ResponseReceiver();
	
	final static int channel = 0;

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
		ShortMessage sm = new ShortMessage();
		sm.setMessage(ShortMessage.NOTE_ON, channel, 60, 127);
		receiver.send(sm, -1);
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		ShortMessage sm2 = new ShortMessage();
		sm2.setMessage(ShortMessage.NOTE_OFF, channel, 60, 127);
		receiver.send(sm2, -1);
	}
	
	/**
	 * Requests the device identity.
	 */
	public void requestIdentity() {
//		SysexMessage sem = new SysexMessage();
//		byte[] message = {0x7E, 0x7F, 0x06, 0x01, (byte)0xF7};
//		sem.setMessage(SysexMessage.SYSTEM_EXCLUSIVE, message, message.length);
//		r.send(sem, -1);
	}
	
}
