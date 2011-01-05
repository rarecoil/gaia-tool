package nl.grauw.gaia_tool;

import nl.grauw.gaia_tool.Parameters.ParameterChange;
import nl.grauw.gaia_tool.mvc.Observable;

public class Value extends Observable {
	
	protected Parameters parameters;
	protected int offset;
	
	public Value(Parameters parameters, int offset) {
		this.parameters = parameters;
		this.offset = offset;
	}
	
	public Parameters getParameters() {
		return parameters;
	}
	
	/**
	 * Determine whether the value has changed based on information received from
	 * a Parameter observable update notification.
	 * 
	 * @param source The source of the update.
	 * @param pc The update’s event object.
	 * @return True if the value has changed.
	 */
	public boolean testChanged(Parameters source, ParameterChange pc) {
		return source == parameters && pc.includes(offset);
	}
	
	/**
	 * Determine whether the value has changed based on information received from
	 * a Parameter observable update notification.
	 * 
	 * This takes plain objects so that the user doesn’t need to cast.
	 * 
	 * @param source The source of the update.
	 * @param pc The update’s event object.
	 * @return True if the value has changed.
	 */
	public boolean testChanged(Object source, Object pc) {
		return source == parameters && pc instanceof ParameterChange &&
				((ParameterChange)pc).includes(offset);
	}
	
}
