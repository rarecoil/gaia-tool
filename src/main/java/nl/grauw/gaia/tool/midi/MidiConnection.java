package nl.grauw.gaia.tool.midi;

public interface MidiConnection {
	
	public void addMidiReceiver(MidiReceiver receiver);
	
	public void removeMidiReceiver(MidiReceiver receiver);
	
	public MidiTransmitter getMidiTransmitter();
	
}
