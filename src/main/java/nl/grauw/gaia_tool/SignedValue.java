package nl.grauw.gaia_tool;

public class SignedValue extends Value {
	
	public SignedValue(ParameterData parameterData, int offset, int min, int max) {
		super(parameterData, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return super.getValue() - 64;
	}
	
}
