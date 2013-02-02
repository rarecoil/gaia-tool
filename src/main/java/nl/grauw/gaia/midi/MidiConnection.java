package nl.grauw.gaia.midi;

public interface MidiConnection {
	
	public void addMidiReceiver(MidiReceiver receiver);
	
	public void removeMidiReceiver(MidiReceiver receiver);
	
	public MidiTransmitter getMidiTransmitter();
	
}
