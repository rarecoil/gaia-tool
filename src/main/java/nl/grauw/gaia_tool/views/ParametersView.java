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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.LayoutStyle.ComponentPlacement;

import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;

public abstract class ParametersView extends JPanel implements AWTObserver, ActionListener {
	private static final long serialVersionUID = 123L;
	
	public class SynchronizeModel extends ToggleButtonModel implements AWTObserver {
		private static final long serialVersionUID = 1L;
		
		public SynchronizeModel() {
			getGaia().addObserver(this);
		}
		
		@Override
		public boolean isSelected() {
			return getGaia().getSynchronize();
		}
		
		@Override
		public void setSelected(boolean b) {
			getGaia().setSynchronize(b);
		}

		@Override
		public void update(Observable source, Object detail) {
			if (source == getGaia() && "synchronize".equals(detail)) {
				fireStateChanged();
			}
		}
		
	}
	
	private JLabel titleLabel;
	private JButton reloadButton;
	private JButton saveButton;
	private JToggleButton syncButton;
	
	public abstract Gaia getGaia();
	
	public abstract void loadParameters();
	
	public abstract void saveParameters();
	
	public abstract String getTitle();
	
	protected abstract boolean isSyncShown();
	
	protected abstract JPanel getParametersContainer();
	
	protected void initComponents() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(
						layout.createSequentialGroup()
							.addComponent(getTitleLabel())
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
							.addComponent(getSyncButton())
							.addComponent(getSaveButton())
							.addComponent(getReloadButton())
					)
					.addComponent(getParametersContainer())
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup(Alignment.CENTER)
							.addComponent(getTitleLabel())
							.addComponent(getSyncButton())
							.addComponent(getSaveButton())
							.addComponent(getReloadButton())
					)
					.addComponent(getParametersContainer())
			);
	}
	
	private JLabel getTitleLabel() {
		if (titleLabel == null) {
			titleLabel = new JLabel();
			titleLabel.setFont(titleLabel.getFont().deriveFont(24f).deriveFont(Font.BOLD));
			titleLabel.setText(getTitle());
		}
		return titleLabel;
	}
	
	private JButton getReloadButton() {
		if (reloadButton == null) {
			reloadButton = new JButton();
			reloadButton.setText("Reload");
			reloadButton.addActionListener(this);
		}
		return reloadButton;
	}
	
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Save");
			saveButton.addActionListener(this);
		}
		return saveButton;
	}
	
	private JToggleButton getSyncButton() {
		if (syncButton == null) {
			syncButton = new JToggleButton();
			syncButton.setText("Sync");
			syncButton.setModel(new SynchronizeModel());
			syncButton.setVisible(isSyncShown());
		}
		return syncButton;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reloadButton) {
			loadParameters();
		}
		if (e.getSource() == saveButton) {
			saveParameters();
		}
	}

}
