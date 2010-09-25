package nl.grauw.gaia_tool;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class ResponseReceiver implements Receiver {

	@Override
	public void send(MidiMessage message, long timeStamp) {
		System.out.println(message.toString() + " - " + timeStamp);
	}

	@Override
	public void close() {
	}

}
