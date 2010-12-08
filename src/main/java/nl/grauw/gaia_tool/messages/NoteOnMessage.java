package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import nl.grauw.gaia_tool.Note;

public class NoteOnMessage extends ShortMessage {
	
	public NoteOnMessage(ShortMessage sm) {
		super(sm.getMessage());
	}
	
	public NoteOnMessage(int channel, Note note, int velocity) throws InvalidMidiDataException {
		super();
		setMessage(NOTE_ON, channel, note.getNoteNumber(), velocity);
	}
	
	public Note getNote() {
		return new Note(getData1());
	}
	
	public int getVelocity() {
		return getData2();
	}
	
	public String toString() {
		return "Note on message. Channel: " + (getChannel() + 1) + ". Note: " + getNote() +
				". Velocity: " + getVelocity() + ".";
	}

}
