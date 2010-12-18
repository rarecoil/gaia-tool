package nl.grauw.gaia_tool;

public class SignedValue16Bit extends Value16Bit {
	
	public SignedValue16Bit(ParameterData parameterData, int offset, int min, int max) {
		super(parameterData, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return super.getValue() - 32768;
	}
	
	@Override
	public void setValueNoCheck(int value) {
		super.setValueNoCheck(value + 32768);
	}
	
}
