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
package nl.grauw.gaia_tool.views.parameters;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.Parameters.ParameterChange;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.Distortion;
import nl.grauw.gaia_tool.parameters.Distortion.DistortionType;
import nl.grauw.gaia_tool.views.EnumComboBox;
import nl.grauw.gaia_tool.views.ValueSpinner;

public class DistortionView extends JPanel implements AWTObserver {
	private static final long serialVersionUID = 1L;
	
	public class MFXParametersInfo extends JLabel implements AWTObserver {
		private static final long serialVersionUID = 1L;
		
		public MFXParametersInfo() {
			super();
			setText(getMFXParametersText());
			parameters.addObserver(this);
		}
		
		private String getMFXParametersText() {
			StringBuilder mfxParameters = new StringBuilder(128);
			for (int i = 1; i <= 32; i++) {
				mfxParameters.append(parameters.getMFXParameter(i));
				mfxParameters.append(" ");
			}
			return "MFX parameters: " + mfxParameters.toString();
		}
		
		@Override
		public void update(Observable source, Object arg) {
			setText(getMFXParametersText());
		}
		
	}

	private Distortion parameters;

	private EnumComboBox distortionTypeControl;
	private ValueSpinner driveControl;
	private ValueSpinner typeControl;
	private ValueSpinner presenceControl;
	private ValueSpinner sampleRateControl;
	private ValueSpinner bitDownControl;
	private ValueSpinner filterControl;
	private ValueSpinner levelControl;
	private MFXParametersInfo mfxParametersInfo;
	
	public DistortionView(Distortion parameters) {
		this.parameters = parameters;
		parameters.addObserver(this);
		initComponents();
	}

	public Parameters getParameters() {
		return parameters;
	}

	private void initComponents() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		SequentialGroup controlsX = layout.createSequentialGroup();
		ParallelGroup controlsY = layout.createParallelGroup();
		if (parameters.getDistortionType() == DistortionType.BIT_CRASH) {
			controlsX.addComponent(getSampleRateControl());
			controlsX.addComponent(getLevelControl());
			controlsX.addComponent(getBitDownControl());
			controlsX.addComponent(getFilterControl());
			controlsY.addComponent(getSampleRateControl());
			controlsY.addComponent(getLevelControl());
			controlsY.addComponent(getBitDownControl());
			controlsY.addComponent(getFilterControl());
		} else if (parameters.getDistortionType() != DistortionType.OFF) {
			controlsX.addComponent(getDriveControl());
			controlsX.addComponent(getLevelControl());
			controlsX.addComponent(getTypeControl());
			controlsX.addComponent(getPresenceControl());
			controlsY.addComponent(getDriveControl());
			controlsY.addComponent(getLevelControl());
			controlsY.addComponent(getTypeControl());
			controlsY.addComponent(getPresenceControl());
		}
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(
						layout.createSequentialGroup()
							.addComponent(getDistortionTypeControl())
						)
					.addGroup(controlsX)
					.addGroup(
						layout.createSequentialGroup()
							.addComponent(getMFXParametersInfo())
						)
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup()
							.addComponent(getDistortionTypeControl())
						)
					.addGroup(controlsY)
					.addGroup(
						layout.createParallelGroup()
							.addComponent(getMFXParametersInfo())
						)
			);
	}
	
	private void reinitComponents() {
		driveControl = null;
		typeControl = null;
		presenceControl = null;
		levelControl = null;
		sampleRateControl = null;
		bitDownControl = null;
		filterControl = null;
		mfxParametersInfo = null;
		removeAll();
		initComponents();
		revalidate();
	}
	
	private EnumComboBox getDistortionTypeControl() {
		if (distortionTypeControl == null) {
			distortionTypeControl = new EnumComboBox(parameters.getDistortionTypeValue(), "Distortion type");
		}
		return distortionTypeControl;
	}
	
	private ValueSpinner getDriveControl() {
		if (driveControl == null) {
			driveControl = new ValueSpinner(parameters.getDrive(), "Drive");
		}
		return driveControl;
	}
	
	private ValueSpinner getLevelControl() {
		if (levelControl == null) {
			levelControl = new ValueSpinner(parameters.getLevel(), "Level");
		}
		return levelControl;
	}
	
	private ValueSpinner getTypeControl() {
		if (typeControl == null) {
			typeControl = new ValueSpinner(parameters.getType(), "Type");
		}
		return typeControl;
	}
	
	private ValueSpinner getPresenceControl() {
		if (presenceControl == null) {
			presenceControl = new ValueSpinner(parameters.getPresence(), "Presence");
		}
		return presenceControl;
	}
	
	private ValueSpinner getSampleRateControl() {
		if (sampleRateControl == null) {
			sampleRateControl = new ValueSpinner(parameters.getSampleRate(), "Sample rate");
		}
		return sampleRateControl;
	}
	
	private ValueSpinner getBitDownControl() {
		if (bitDownControl == null) {
			bitDownControl = new ValueSpinner(parameters.getBitDown(), "Bit down");
		}
		return bitDownControl;
	}
	
	private ValueSpinner getFilterControl() {
		if (filterControl == null) {
			filterControl = new ValueSpinner(parameters.getFilter(), "Filter");
		}
		return filterControl;
	}
	
	private MFXParametersInfo getMFXParametersInfo() {
		if (mfxParametersInfo == null) {
			mfxParametersInfo = new MFXParametersInfo();
		}
		return mfxParametersInfo;
	}
	
	@Override
	public void update(Observable source, Object arg) {
		if (source instanceof Distortion && arg instanceof ParameterChange) {
			update((Distortion) source, (ParameterChange) arg);
		}
	}
	
	private void update(Distortion source, ParameterChange arg) {
		if (arg.includes(parameters.getDistortionTypeValue().getOffset())) {
			reinitComponents();
		}
	}

}
