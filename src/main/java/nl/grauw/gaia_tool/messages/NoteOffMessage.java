package nl.grauw.gaia_tool.messages;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import nl.grauw.gaia_tool.Note;

public class NoteOffMessage extends ShortMessage {
	
	public NoteOffMessage(ShortMessage sm) {
		super(sm.getMessage());
	}
	
	public NoteOffMessage(int channel, Note note, int velocity) throws InvalidMidiDataException {
		super();
		setMessage(NOTE_OFF, channel, note.getNoteNumber(), velocity);
	}
	
	public Note getNote() {
		return new Note(getData1());
	}
	
	public int getVelocity() {
		return getData2();
	}
	
	public String toString() {
		return "Note off message. Channel: " + (getChannel() + 1) + ". Note: " + getNote() +
				". Velocity: " + getVelocity() + ".";
	}

}
