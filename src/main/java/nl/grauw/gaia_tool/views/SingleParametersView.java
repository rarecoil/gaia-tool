package nl.grauw.gaia_tool.views;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.mvc.Observable;

public abstract class SingleParametersView extends ParameterGroupView {
	private static final long serialVersionUID = 1L;
	
	private JPanel parametersContainer;
	private JTextArea parameterArea;
	private JScrollPane parameterScrollPane;
	
	public abstract Parameters getParameters();
	
	@Override
	public void update(Observable source, Object arg) {
		if (source == getParameters()) {
			parameterArea.setText(getParameters().toString());
		} else {
			// register self as observer on parameters
			if (getParameters() != null) {
				if (!getParameters().hasObserver(this))
					getParameters().addObserver(this);
				update(getParameters(), null);
			}
		}
		
	}
	
	protected JPanel getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JPanel();
			parametersContainer.setLayout(new BoxLayout(parametersContainer, BoxLayout.X_AXIS));
			parametersContainer.add(getParameterScrollPane());
		}
		return parametersContainer;
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
			DefaultCaret caret = (DefaultCaret)parameterArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
			if (getParameters() == null) {
				loadParameters();
			} else {
				parameterArea.setText(getParameters().toString());
			}
		}
		return parameterArea;
	}
	
}
