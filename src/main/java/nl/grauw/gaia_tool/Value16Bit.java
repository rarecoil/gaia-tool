package nl.grauw.gaia_tool;

public class Value16Bit extends Value {
	
	public Value16Bit(ParameterData parameterData, int offset, int min, int max) {
		super(parameterData, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return parameterData.get16BitValue(offset);
	}
	
}
