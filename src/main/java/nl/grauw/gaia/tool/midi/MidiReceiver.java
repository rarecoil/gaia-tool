package nl.grauw.gaia.tool.midi;

import nl.grauw.gaia.tool.messages.Message;

public interface MidiReceiver {
	
	public void receive(Message message);
	
}
