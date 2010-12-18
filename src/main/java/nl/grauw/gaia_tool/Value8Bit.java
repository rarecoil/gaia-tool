package nl.grauw.gaia_tool;

public class Value8Bit extends Value {
	
	public Value8Bit(ParameterData parameterData, int offset, int min, int max) {
		super(parameterData, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return parameterData.get8BitValue(offset);
	}
	
}
