package nl.grauw.gaia_tool;

public class Int16BitValue extends IntValue {
	
	public Int16BitValue(Parameters parameters, int offset, int min, int max) {
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
