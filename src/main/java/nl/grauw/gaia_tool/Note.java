package nl.grauw.gaia_tool;

public class Note {
	
	public enum NoteName {
		C("C"), C_SHARP("C#"), D("D"), D_SHARP("D#"), E("E"), F("F"), F_SHARP("F#"),
		G("G"), G_SHARP("G#"), A("A"), A_SHARP("A#"), B("B");
		
		String label;
		
		NoteName(String label) {
			this.label = label;
		}
		
		public String toString() {
			return label;
		}
	}
	
	private NoteName note;
	private int octave;
	
	/**
	 * 
	 * @param noteNumber The note number as used in the MIDI standard.
	 */
	public Note(int noteNumber) {
		this(NoteName.values()[noteNumber % 12], noteNumber / 12 - 1);
	}
	
	public Note(NoteName note, int octave) {
		this.note = note;
		this.octave = octave;
	}
	
	public NoteName getNote() {
		return note;
	}
	
	public int getOctave() {
		return octave;
	}
	
	public int getNoteNumber() {
		return (octave + 1) * 12 + note.ordinal();
	}
	
	public String toString() {
		return note + " " + octave;
	}

}
