package nl.grauw.gaia_tool;

import nl.grauw.gaia_tool.mvc.Observable;

public class Log extends Observable {
	
	private StringBuffer log;
	
	public Log() {
		log = new StringBuffer();
	}
	
	public void log(String message) {
		log.append(message);
		log.append("\n");
		
		notifyObservers();
	}
	
	public String getLog() {
		return log.toString();
	}
	
}
