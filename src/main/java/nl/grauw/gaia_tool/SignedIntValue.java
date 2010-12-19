package nl.grauw.gaia_tool;

public class SignedIntValue extends IntValue {
	
	public SignedIntValue(Parameters parameters, int offset, int min, int max) {
		super(parameters, offset, min, max);
	}
	
	@Override
	public int getValue() {
		return super.getValue() - 64;
	}
	
	@Override
	public void setValueNoCheck(int value) {
		super.setValueNoCheck(value + 64);
	}
	
}
