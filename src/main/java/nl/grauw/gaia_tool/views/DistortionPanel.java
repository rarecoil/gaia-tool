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

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.views.parameters.DistortionView;

public class DistortionPanel extends ParametersView {
	private static final long serialVersionUID = 1L;

	private Patch patch;

	private JPanel parametersContainer;
	private DistortionView distortionView;
	
	public DistortionPanel(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		if (patch.getDistortion() == null)
			loadParameters();
		initComponents();
	}
	
	@Override
	public Gaia getGaia() {
		return patch.getGaia();
	}
	
	@Override
	public void loadParameters() {
		patch.loadDistortion();
	}

	@Override
	public void saveParameters() {
	}

	@Override
	public String getTitle() {
		return "Patch distortion";
	}

	@Override
	public void update(Observable source, Object arg) {
		if (arg == "distortion") {
			getParametersContainer().removeAll();
			if (getDistortionView() != null)
				parametersContainer.add(getDistortionView());
			getParametersContainer().revalidate();
		}
	}

	@Override
	protected JPanel getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JPanel();
			parametersContainer.setLayout(new BoxLayout(parametersContainer, BoxLayout.X_AXIS));
			if (getDistortionView() != null) {
				parametersContainer.add(getDistortionView());
			}
		}
		return parametersContainer;
	}

	protected DistortionView getDistortionView() {
		if (distortionView == null || distortionView.getParameters() != patch.getDistortion()) {
			if (patch.getDistortion() != null)
				distortionView = new DistortionView(patch.getDistortion());
		}
		return distortionView;
	}

}
