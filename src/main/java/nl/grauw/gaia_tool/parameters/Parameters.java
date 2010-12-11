package nl.grauw.gaia_tool.parameters;

import nl.grauw.gaia_tool.mvc.Observable;

public class Parameters extends Observable {
	
	protected byte[] addressMap;
	
	public Parameters(byte[] addressMap) {
		this.addressMap = addressMap;
	}
	
}