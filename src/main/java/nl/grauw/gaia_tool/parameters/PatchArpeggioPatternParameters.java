package nl.grauw.gaia_tool.parameters;

import nl.grauw.gaia_tool.Note;
import nl.grauw.gaia_tool.Value;

public class PatchArpeggioPatternParameters extends Parameters {
	
	public PatchArpeggioPatternParameters(byte[] addressMap) {
		super(addressMap);
		
		if (addressMap.length < 0x42)
			throw new RuntimeException("Address map size mismatch.");
	}
	
	/**
	 * Get the pattern’s original note (C-4 is the base).
	 * Note number 128 (G#9) means OFF.
	 * @return The pattern original note.
	 */
	public Note getOriginalNote() {
		return new Note(addressMap[0x00] << 4 | addressMap[0x01]);
	}
	
	public Value getStepData(int step) {
		if (step < 1 || step > 32)
			throw new RuntimeException("Invalid step number.");
		
		return new Value(addressMap[step * 2] << 4 | addressMap[step * 2 + 1], 0, 128);
	}
	
	public String toString() {
		StringBuilder stepData = new StringBuilder(128);
		for (int i = 1; i <= 32; i++) {
			stepData.append(getStepData(i));
			stepData.append(" ");
		}
		
		return "Patch arpeggio pattern parameters:\n" +
				String.format("Original note: %s\n", getOriginalNote().getNoteNumber() != 129 ? getOriginalNote() : "OFF") +
				String.format("Step data: %s\n", stepData);
	}
	
}
