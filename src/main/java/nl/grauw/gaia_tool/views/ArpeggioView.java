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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToggleButton.ToggleButtonModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Note;
import nl.grauw.gaia_tool.NoteValue;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.IntValue;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioPattern;

public class ArpeggioView extends JPanel implements AWTObserver, ActionListener {
	
	public class ArpeggioModel extends AbstractTableModel implements AWTObserver {
		private static final long serialVersionUID = 1L;
		
		Patch patch;
		
		public ArpeggioModel(Patch patch) {
			this.patch = patch;
			patch.addObserver(this);
			addCommonObserver();
			addPatternObservers();
		}
		
		private void addCommonObserver() {
			Parameters common = patch.getArpeggioCommon();
			if (common != null && !common.hasObserver(this)) {
				common.addObserver(this);
			}
		}
		
		private void addPatternObservers() {
			for (int note = 1; note <= 16; note++) {
				Parameters pattern = patch.getArpeggioPattern(note);
				if (pattern != null && !pattern.hasObserver(this)) {
					pattern.addObserver(this);
				}
			}
		}
		
		@Override
		public int getRowCount() {
			return 16;
		}

		@Override
		public int getColumnCount() {
			ArpeggioCommon pacp = patch.getArpeggioCommon();
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
			ArpeggioPattern papp = patch.getArpeggioPattern(row + 1);
			if (papp == null)
				return "";
			if (column == 0) {
				Note originalNote = papp.getOriginalNote().getValue();
				return originalNote.getNoteNumber() == 128 ? "OFF" : originalNote;
			} else {
				int velocity = papp.getStepData(column).getValue();
//				if (velocity == 0)
//					return "Rest";
				if (velocity == 128)
					return "Tie";
				return velocity;
			}
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (aValue instanceof String) {
				setValueAt((String) aValue, rowIndex, columnIndex);
			}
		}
		
		private void setValueAt(String aValue, int rowIndex, int columnIndex) {
			aValue = aValue.trim().toUpperCase();
			ArpeggioPattern pattern = patch.getArpeggioPattern(rowIndex + 1);
			if (columnIndex == 0) {
				NoteValue originalNote = pattern.getOriginalNote();
				if (aValue.equals("OFF")) {
					originalNote.setValue(new Note(128));
				} else {
					try {
						originalNote.setValue(new Note(aValue));
					} catch(IllegalArgumentException e) {
					}
				}
			} else {
				IntValue stepData = pattern.getStepData(columnIndex);
				if (aValue.equals("TIE")) {
					stepData.setValue(128);
				} else if (aValue.equals("REST")) {
					stepData.setValue(0);
				} else {
					try {
						stepData.setValue(new Integer(aValue));
					} catch(IllegalArgumentException e) {
					}
				}
			}
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public void update(Observable o, Object arg) {
			if (o == patch) {
				if ("arpeggioCommon".equals(arg)) {
					addCommonObserver();
					fireTableStructureChanged();
				} else if ("arpeggioPatterns".equals(arg)) {
					addPatternObservers();
					fireTableDataChanged();
				}
			} else if (o == patch.getArpeggioCommon()) {
				fireTableStructureChanged();
			} else if (o instanceof ArpeggioPattern) {
				for (int note = 1; note <= 16; note++) {
					if (o == patch.getArpeggioPattern(note)) {
						fireTableRowsUpdated(note - 1, note - 1);
						break;
					}
				}
			}
		}
	}
	
	public class SynchronizeModel extends ToggleButtonModel implements Observer {
		private static final long serialVersionUID = 1L;
		
		private Gaia gaia;
		
		public SynchronizeModel(Gaia g) {
			gaia = g;
			gaia.addObserver(this);
		}
		
		@Override
		public boolean isSelected() {
			return gaia.getSynchronize();
		}
		
		@Override
		public void setSelected(boolean b) {
			gaia.setSynchronize(b);
		}

		@Override
		public void update(Observable source, Object arg) {
			if (source == gaia && "synchronize".equals(arg)) {
				fireStateChanged();
			}
		}
		
	}
	
	private static final long serialVersionUID = 1L;

	private Patch patch;
	
	private JLabel titleLabel;
	private JButton reloadButton;
	private JButton saveButton;
	private JToggleButton syncButton;
	private JPanel parametersContainer;
	private ArpeggioCommonView parametersView;
	private JScrollPane patternScrollPane;
	private JTable patternTable;
	private JTextField editField;
	
	public ArpeggioView(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		if (patch.getArpeggioCommon() == null)
			loadParameters();
		initComponents();
	}
	
	private void loadParameters() {
		patch.loadArpeggioAll();
	}
	
	private void saveParameters() {
		patch.saveArpeggioAll();
	}

	private String getTitle() {
		return "Patch arpeggio";
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == patch) {
			if ("arpeggioCommon".equals(arg)) {
				updateParametersView();
			}
		}
	}
	
	private void updateParametersView() {
		JPanel pc = getParametersContainer();
		ArpeggioCommonView pv = getParametersView();
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
							.addComponent(getSyncButton())
							.addComponent(getSaveButton())
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
							.addComponent(getSyncButton())
							.addComponent(getSaveButton())
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
			ArpeggioCommonView pv = getParametersView();
			if (pv != null)
				parametersContainer.add(pv);
		}
		return parametersContainer;
	}
	
	private ArpeggioCommonView getParametersView() {
		ArpeggioCommon pacp = patch.getArpeggioCommon();
		if (parametersView == null || parametersView.getModel() != pacp) {
			if (pacp != null)
				parametersView = new ArpeggioCommonView(pacp);
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
			syncButton.setModel(new SynchronizeModel(patch.getGaia()));
		}
		return syncButton;
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
			TableModel model = new ArpeggioModel(patch);
			patternTable = new JTable(model);
			patternTable.setFillsViewportHeight(true);
			patternTable.setDefaultEditor(Object.class, new DefaultCellEditor(getEditField()) {
				private static final long serialVersionUID = 1L;
				@Override
				public Component getTableCellEditorComponent(JTable table,
						Object value, boolean isSelected, int row, int column) {
					// selects the text when starting to edit
					JTextField editField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
					editField.selectAll();
					return editField;
				}
			});
		}
		return patternTable;
	}
	
	private JTextField getEditField() {
		if (editField == null) {
			editField = new JTextField() {
				private static final long serialVersionUID = 1L;
				@Override
				// selects the text on focus
				public void requestFocus() {
					selectAll();
					super.requestFocus();
				}
			};
			editField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}
		return editField;
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
