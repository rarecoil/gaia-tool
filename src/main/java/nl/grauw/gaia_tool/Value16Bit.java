package nl.grauw.gaia_tool;

public class Value16Bit extends Value {
	
	public Value16Bit(Parameters parameters, int offset, int min, int max) {
		super(parameters, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return parameters.get16BitValue(offset);
	}
	
	@Override
	public void setValueNoCheck(int value) {
		parameters.set16BitValue(offset, value);
	}
	
}
