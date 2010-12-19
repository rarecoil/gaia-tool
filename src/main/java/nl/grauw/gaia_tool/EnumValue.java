package nl.grauw.gaia_tool;

public class EnumValue<T extends Enum<T>> extends ValueBase {
	
	protected T[] choices;
	
	public EnumValue(Parameters parameters, int offset, T[] choices) {
		super(parameters, offset);
		this.choices = choices;
	}
	
	public T getValue() {
		return choices[parameters.getValue(offset)];
	}
	
	public void setValue(T value) {
		parameters.setValue(offset, value.ordinal());
	}
	
	/**
	 * Sets the value to an object.
	 * This setter takes any object, to work around impossibility to reliably cast to generics.
	 * @param value The value to set. Must be one of getChoices().
	 */
	public void setValue(Object value) {
		for (T choice : choices) {
			if (choice == value) {
				parameters.setValue(offset, choice.ordinal());
				return;
			}
		}
		throw new IllegalArgumentException("Provided type ");
	}
	
	public T[] getChoices() {
		return choices;
	}
	
	public String toString() {
		return getValue().toString();
	}
	
}
