package nl.grauw.gaia_tool;

public class Value {
	
	private int value;
	private int min;
	private int max;
	
	public Value(int value, int min, int max) {
		if (value < min || value > max)
			throw new RuntimeException("Illegal value.");
		this.value = value;
		this.min = min;
		this.max = max;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		if (value < min || value > max)
			throw new RuntimeException("Illegal value.");
		this.value = value;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public String toString() {
		return String.valueOf(value);
	}
	
}
