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

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import nl.grauw.gaia_tool.Note;
import nl.grauw.gaia_tool.PatchParameterGroup;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.PatchArpeggioCommonParameters;
import nl.grauw.gaia_tool.parameters.PatchArpeggioPatternParameters;

public class PatchArpeggioView extends JPanel implements Observer, ActionListener {
	
	public class ArpeggioModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
		PatchParameterGroup parameterGroup;
		
		public ArpeggioModel(PatchParameterGroup ppg) {
			parameterGroup = ppg;
		}
		
		@Override
		public int getRowCount() {
			return 16;
		}

		@Override
		public int getColumnCount() {
			PatchArpeggioCommonParameters pacp = parameterGroup.getArpeggioCommon();
			if (pacp == null)
				return 1;
			return pacp.getEndStep().getValue() + 1;
		}
		
		public String getColumnName(int column) {
			if (column == 0)
				return "Note";
			return String.valueOf(column);
		}

		@Override
		public Object getValueAt(int row, int column) {
			PatchArpeggioPatternParameters papp = parameterGroup.getArpeggioPattern(row + 1);
			if (papp == null)
				return "";
			if (column == 0) {
				Note originalNote = papp.getOriginalNote();
				return originalNote.getNoteNumber() == 128 ? "OFF" : originalNote;
			}
			return papp.getStepData(column);
		}
	}
	
	private static final long serialVersionUID = 1L;

	private PatchParameterGroup parameterGroup;
	
	private JLabel titleLabel;
	private JButton reloadButton;
	private JPanel parametersContainer;
	private PatchArpeggioCommonParametersView parametersView;
	private JScrollPane patternScrollPane;
	private JTable patternTable;
	
	public PatchArpeggioView(PatchParameterGroup ppg) {
		parameterGroup = ppg;
		ppg.addObserver(this);
		if (ppg.getArpeggioCommon() == null)
			loadParameters();
		initComponents();
	}
	
	private void loadParameters() {
		parameterGroup.loadArpeggioAll();
	}

	private String getTitle() {
		return "Patch arpeggio";
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == parameterGroup) {
			if ("arpeggioCommon".equals(arg)) {
				updateParametersView();
			}
			getPatternTable().tableChanged(new TableModelEvent(getPatternTable().getModel()));
		}
	}
	
	private void updateParametersView() {
		JPanel pc = getParametersContainer();
		PatchArpeggioCommonParametersView pv = getParametersView();
		if (pc.getComponentCount() == 0 || pc.getComponent(0) != pv) {
			pc.removeAll();
			if (pv != null)
				pc.add(pv);
			pc.revalidate();
		}
	}
	
	private void initComponents() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(
						layout.createSequentialGroup()
							.addComponent(getTitleLabel())
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.PREFERRED_SIZE, Integer.MAX_VALUE)
							.addComponent(getReloadButton())
					)
					.addComponent(getParametersContainer())
					.addComponent(getPatternScrollPane())
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup(Alignment.CENTER)
							.addComponent(getTitleLabel())
							.addComponent(getReloadButton())
					)
					.addComponent(getParametersContainer(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(getPatternScrollPane())
			);
	}
	
	private JPanel getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JPanel();
			parametersContainer.setLayout(new BoxLayout(parametersContainer, BoxLayout.X_AXIS));
			PatchArpeggioCommonParametersView pv = getParametersView();
			if (pv != null)
				parametersContainer.add(pv);
		}
		return parametersContainer;
	}
	
	private PatchArpeggioCommonParametersView getParametersView() {
		PatchArpeggioCommonParameters pacp = parameterGroup.getArpeggioCommon();
		if (parametersView == null || parametersView.getModel() != pacp) {
			if (pacp != null)
				parametersView = new PatchArpeggioCommonParametersView(pacp);
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
	
	private JButton getReloadButton() {
		if (reloadButton == null) {
			reloadButton = new JButton();
			reloadButton.setText("Reload");
			reloadButton.addActionListener(this);
		}
		return reloadButton;
	}
	
	private JScrollPane getPatternScrollPane() {
		if (patternScrollPane == null) {
			patternScrollPane = new JScrollPane();
			patternScrollPane.setViewportView(getPatternTable());
		}
		return patternScrollPane;
	}
	
	private JTable getPatternTable() {
		if (patternTable == null) {
			TableModel model = new ArpeggioModel(parameterGroup);
			patternTable = new JTable(model);
			patternTable.setFillsViewportHeight(true);
		}
		return patternTable;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reloadButton) {
			loadParameters();
		}
	}

}
