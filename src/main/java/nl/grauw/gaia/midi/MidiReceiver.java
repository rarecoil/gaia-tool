package nl.grauw.gaia.midi;

import nl.grauw.gaia.midi.messages.Message;

public interface MidiReceiver {
	
	public void receive(Message message);
	
}
