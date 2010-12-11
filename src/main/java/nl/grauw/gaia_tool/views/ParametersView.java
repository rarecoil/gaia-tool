package nl.grauw.gaia_tool.views;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.Parameters;

//VS4E -- DO NOT REMOVE THIS LINE!
public class ParametersView extends JPanel implements Observer {
	
	private Parameters parameters;
	
	private static final long serialVersionUID = 123L;
	private JTextArea parameterArea;
	private JScrollPane parameterScrollPane;
	
	public ParametersView() {
		initComponents();
	}
	
	public ParametersView(Parameters p) {
		this();
		setModel(p);
	}
	
	public void setModel(Parameters p) {
		if (parameters != null)
			parameters.removeObserver(this);
		parameters = p;
		parameters.addObserver(this);
		update(p, null);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == parameters) {
			parameterArea.setText(parameters.toString());
		}
	}
	
	private void initComponents() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(getParameterScrollPane());
	}
	
	private JScrollPane getParameterScrollPane() {
		if (parameterScrollPane == null) {
			parameterScrollPane = new JScrollPane();
			parameterScrollPane.setViewportView(getParameterArea());
		}
		return parameterScrollPane;
	}
	
	private JTextArea getParameterArea() {
		if (parameterArea == null) {
			parameterArea = new JTextArea();
			parameterArea.setEditable(false);
			parameterArea.setLineWrap(true);
			parameterArea.setWrapStyleWord(true);
		}
		return parameterArea;
	}
	
}