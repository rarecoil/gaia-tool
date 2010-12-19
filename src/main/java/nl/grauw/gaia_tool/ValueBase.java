package nl.grauw.gaia_tool;

import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;

public class ValueBase extends Observable implements Observer {
	
	protected Parameters parameters;
	protected int offset;
	
	public ValueBase(Parameters parameters, int offset) {
		this.parameters = parameters;
		this.offset = offset;
		parameters.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == parameters && arg instanceof Integer && (Integer)arg == offset) {
			notifyObservers();
		}
	}
	
}
