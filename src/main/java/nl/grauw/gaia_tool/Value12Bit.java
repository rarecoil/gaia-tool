package nl.grauw.gaia_tool;

public class Value12Bit extends Value {
	
	public Value12Bit(ParameterData parameterData, int offset, int min, int max) {
		super(parameterData, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return parameterData.get12BitValue(offset);
	}
	
}
