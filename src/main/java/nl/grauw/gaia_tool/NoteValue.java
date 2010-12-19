package nl.grauw.gaia_tool;

public class NoteValue extends Value {
	
	public NoteValue(Parameters parameters, int offset) {
		super(parameters, offset);
	}
	
	public Note getValue() {
		return new Note(parameters.get8BitValue(offset));
	}
	
	public void setValue(Note value) {
		parameters.set8BitValue(offset, value.getNoteNumber());
	}
	
	public String toString() {
		return getValue().toString();
	}
	
}
