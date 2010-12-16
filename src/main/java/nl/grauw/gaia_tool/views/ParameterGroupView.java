/*
 * Copyright 2010 Laurens Holst
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.grauw.gaia_tool.views;

import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.Parameters;

public abstract class ParameterGroupView extends JPanel implements Observer {
	
	private static final long serialVersionUID = 123L;
	private JLabel titleLabel;
	private JPanel parametersContainer;
	private ParametersView parametersView;
	
	public abstract Parameters getParameters();
	
	public abstract void loadParameters();
	
	public abstract String getTitle();
	
	@Override
	public void update(Observable o, Object arg) {
		JPanel pc = getParametersContainer();
		ParametersView pv = getParametersView();
		if (pc.getComponent(0) != pv) {
			pc.removeAll();
			pc.add(pv);
			pc.revalidate();
		}
	}
	
	protected void initComponents() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(getTitleLabel())
					.addComponent(getParametersContainer())
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(getTitleLabel())
					.addComponent(getParametersContainer())
			);
	}
	
	private JPanel getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JPanel();
			parametersContainer.setLayout(new BoxLayout(parametersContainer, BoxLayout.X_AXIS));
			parametersContainer.add(getParametersView());
		}
		return parametersContainer;
	}
	
	private ParametersView getParametersView() {
		if (parametersView == null || parametersView.getModel() != getParameters()) {
			if (getParameters() == null)
				loadParameters();
			parametersView = new ParametersView(getParameters());
		}
		return parametersView;
	}
	
	private JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel();
			titleLabel.setFont(titleLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD));
			titleLabel.setText(getTitle());
		}
		return titleLabel;
	}

}
