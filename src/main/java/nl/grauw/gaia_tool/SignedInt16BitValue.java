package nl.grauw.gaia_tool;

public class SignedInt16BitValue extends Int16BitValue {
	
	public SignedInt16BitValue(Parameters parameters, int offset, int min, int max) {
		super(parameters, offset, min, max);
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
