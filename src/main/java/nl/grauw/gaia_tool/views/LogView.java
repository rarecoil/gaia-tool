package nl.grauw.gaia_tool.views;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import nl.grauw.gaia_tool.Log;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;

//VS4E -- DO NOT REMOVE THIS LINE!
public class LogView extends JPanel implements Observer {
	
	private Log log;

	private static final long serialVersionUID = 1L;
	private JTextArea logArea;
	private JScrollPane logScrollPane;

	public LogView() {
		initComponents();
	}
	
	public void setModel(Log l) {
		if (log != null)
			log.removeObserver(this);
		log = l;
		log.addObserver(this);
		update(log, null);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == log) {
			int logTextLength = log.getLog().length();
			int logAreaLength = logArea.getText().length();
			if (logTextLength > logAreaLength) {
				logArea.append(log.getLog().substring(logAreaLength));
			} else if (logTextLength < logAreaLength) {
				throw new RuntimeException("Length mismatch. Something is amiss here.");
			}
		}
	}

	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(getLogScrollPane());
	}

	private JScrollPane getLogScrollPane() {
		if (logScrollPane == null) {
			logScrollPane = new JScrollPane();
			logScrollPane.setViewportView(getLogArea());
		}
		return logScrollPane;
	}

	private JTextArea getLogArea() {
		if (logArea == null) {
			logArea = new JTextArea();
			logArea.setEditable(false);
			logArea.setLineWrap(true);
			logArea.setWrapStyleWord(true);
			DefaultCaret caret = (DefaultCaret) logArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);	// scroll along with text changes
		}
		return logArea;
	}

}
