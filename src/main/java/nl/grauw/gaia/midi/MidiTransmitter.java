package nl.grauw.gaia.midi;

import nl.grauw.gaia.midi.messages.Message;

public interface MidiTransmitter {
	
	public void send(Message message);
	
}
