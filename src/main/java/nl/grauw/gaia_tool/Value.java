package nl.grauw.gaia_tool;

import nl.grauw.gaia_tool.Parameters.ParameterChange;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;

public class Value extends Observable implements Observer {
	
	protected Parameters parameters;
	protected int offset;
	
	public Value(Parameters parameters, int offset) {
		this.parameters = parameters;
		this.offset = offset;
		parameters.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == parameters && arg instanceof ParameterChange && ((ParameterChange) arg).includes(offset)) {
			notifyObservers();
		}
	}
	
}
