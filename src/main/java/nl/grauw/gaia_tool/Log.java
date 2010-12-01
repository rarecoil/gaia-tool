package nl.grauw.gaia_tool;

import java.util.Observable;

public class Log extends Observable {
	
	private StringBuffer log;
	
	public Log() {
		log = new StringBuffer();
	}
	
	public void log(String message) {
		log.append(message);
		log.append("\n");
		
		setChanged();
		notifyObservers();
	}
	
	public String getLog() {
		return log.toString();
	}
	
}
