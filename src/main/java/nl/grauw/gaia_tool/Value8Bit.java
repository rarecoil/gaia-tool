package nl.grauw.gaia_tool;

public class Value8Bit extends Value {
	
	public Value8Bit(Parameters parameters, int offset, int min, int max) {
		super(parameters, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return parameters.get8BitValue(offset);
	}
	
	@Override
	public void setValueNoCheck(int value) {
		parameters.set8BitValue(offset, value);
	}
	
}
