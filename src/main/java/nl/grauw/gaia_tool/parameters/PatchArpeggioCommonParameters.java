package nl.grauw.gaia_tool.parameters;

import nl.grauw.gaia_tool.Value;

public class PatchArpeggioCommonParameters extends Parameters {
	
	public enum ArpeggioGrid {
		_04_, _08_, _08L, _08H, _08t, _16_, _16L, _16H, _16t;
		
		public String toString() {
			return super.toString().substring(1);
		}
	}
	
	public enum ArpeggioDuration {
		_30, _40, _50, _60, _70, _80, _90, _100, _120, FUL;
		
		public String toString() {
			if (this == FUL)
				return "FUL";
			return super.toString().substring(1);
		}
	}
	
	public enum ArpeggioMotif {
		UP_L, UP_L_AND_H, UP__, DOWN_L, DOWN_L_AND_H, DOWN__,
		UP_AND_DOWN_L, UP_AND_DOWN_L_AND_H, UP_AND_DOWN__, RANDOM_L, RANDOM__, PHRASE
	}
	
	public PatchArpeggioCommonParameters(byte[] addressMap) {
		super(addressMap);
		
		if (addressMap.length < 0x08)
			throw new RuntimeException("Address map size mismatch.");
	}
	
	public ArpeggioGrid getArpeggioGrid() {
		return ArpeggioGrid.values()[addressMap[0x00]];
	}
	
	public ArpeggioDuration getArpeggioDuration() {
		return ArpeggioDuration.values()[addressMap[0x01]];
	}
	
	public ArpeggioMotif getArpeggioMotif() {
		return ArpeggioMotif.values()[addressMap[0x02]];
	}
	
	public Value getArpeggioOctaveRange() {
		return new Value(addressMap[0x03] - 64, -3, +3);
	}
	
	public Value getArpeggioAccentRate() {
		return new Value(addressMap[0x04], 0, 100);
	}
	
	// 0 ... 127 (REAL, 1 ... 127)
	public Value getArpeggioVelocity() {
		return new Value(addressMap[0x05], 0, 127);
	}
	
	public Value getEndStep() {
		return new Value(addressMap[0x06] << 4 | addressMap[0x07], 1, 32);
	}
	
	public String toString() {
		return "Patch arpeggio common parameters:\n" +
				String.format("Arpeggio grid: %s\n", getArpeggioGrid()) +
				String.format("Arpeggio duration: %s\n", getArpeggioDuration()) +
				String.format("Arpeggio motif: %s\n", getArpeggioMotif()) +
				String.format("Arpeggio octave range: %s\n", getArpeggioOctaveRange()) +
				String.format("Arpeggio accent rate: %s\n", getArpeggioAccentRate()) +
				String.format("Arpeggio velocity: %s\n", getArpeggioVelocity()) +
				String.format("End step: %s\n", getEndStep());
	}
	
}
