package nl.grauw.gaia_tool;

public class Value12Bit extends Value {
	
	public Value12Bit(Parameters parameters, int offset, int min, int max) {
		super(parameters, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return parameters.get12BitValue(offset);
	}
	
	@Override
	public void setValueNoCheck(int value) {
		parameters.set12BitValue(offset, value);
	}
	
}
