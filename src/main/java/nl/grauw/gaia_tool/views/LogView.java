package nl.grauw.gaia_tool.views;

import java.awt.TextArea;
import java.util.Observable;
import java.util.Observer;

import nl.grauw.gaia_tool.Log;

public class LogView extends TextArea implements Observer {
	
	private static final long serialVersionUID = -2260786939640623710L;
	
	private Log log;
	
	public LogView(Log l) {
		super("", 24, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);

		// assign model
		log = l;
		
		// observe models
		log.addObserver(this);
		update(log, null);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Log) {
			update((Log) o, arg);
		}
	}
	
	public void update(Log l, Object arg) {
		append(l.getLog().substring(getText().length()));
	}
	
}
