package nl.grauw.gaia.tool.midi;

import nl.grauw.gaia.tool.messages.Message;

public interface MidiTransmitter {
	
	public void send(Message message);
	
}
