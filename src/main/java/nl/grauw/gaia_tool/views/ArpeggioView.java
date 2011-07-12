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
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Note;
import nl.grauw.gaia_tool.NoteValue;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.IntValue;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioPattern;
import nl.grauw.gaia_tool.views.parameters.ArpeggioCommonView;

public class ArpeggioView extends ParametersView implements AWTObserver, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private Patch patch;
	
	private JPanel parametersContainer;
	private JPanel arpeggioCommonContainer;
	private ArpeggioCommonView arpeggioCommonView;
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

	@Override
	public Gaia getGaia() {
		if (patch instanceof GaiaPatch)
			return ((GaiaPatch)patch).getGaia();
		return null;
	}
	
	@Override
	public void loadParameters() {
		if (patch instanceof GaiaPatch) {
			((GaiaPatch)patch).loadArpeggioAll();
		}
	}
	
	@Override
	public void saveParameters() {
		if (patch instanceof GaiaPatch) {
			((GaiaPatch)patch).saveModifiedParameters();
		}
	}

	@Override
	public String getTitle() {
		return "Patch arpeggio";
	}
	
	@Override
	public void update(Observable source, Object detail) {
		if ("arpeggioCommon".equals(detail) && source == patch) {
			JPanel container = getArpeggioCommonContainer();
			ArpeggioCommonView acv = getArpeggioCommonView();
			if (container.getComponentCount() == 0 || container.getComponent(0) != acv) {
				container.removeAll();
				if (acv != null)
					container.add(acv);
				container.revalidate();
			}
			if (patch.getArpeggioCommon() == null && isShowing()) {
				loadParameters();
			}
		}
	}
	
	@Override
	protected JComponent getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JPanel();
			GroupLayout layout = new GroupLayout(parametersContainer);
			parametersContainer.setLayout(layout);
			layout.setAutoCreateGaps(true);
			layout.setHorizontalGroup(
					layout.createParallelGroup()
						.addComponent(getArpeggioCommonContainer())
						.addComponent(getPatternScrollPane())
				);
			layout.setVerticalGroup(
					layout.createSequentialGroup()
						.addComponent(getArpeggioCommonContainer(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(getPatternScrollPane())
				);
		}
		return parametersContainer;
	}
	
	private JPanel getArpeggioCommonContainer() {
		if (arpeggioCommonContainer == null) {
			arpeggioCommonContainer = new JPanel();
			arpeggioCommonContainer.setLayout(new BoxLayout(arpeggioCommonContainer, BoxLayout.Y_AXIS));
			ArpeggioCommonView pv = getArpeggioCommonView();
			if (pv != null)
				arpeggioCommonContainer.add(pv);
		}
		return arpeggioCommonContainer;
	}
	
	private ArpeggioCommonView getArpeggioCommonView() {
		ArpeggioCommon pacp = patch.getArpeggioCommon();
		if (arpeggioCommonView == null || arpeggioCommonView.getModel() != pacp) {
			if (pacp != null)
				arpeggioCommonView = new ArpeggioCommonView(pacp);
		}
		return arpeggioCommonView;
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
			TableModel model = new ArpeggioModel();
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
	protected boolean isSyncShown() {
		return patch instanceof TemporaryPatch;
	}
	
	public class ArpeggioModel extends AbstractTableModel implements AWTObserver {
		private static final long serialVersionUID = 1L;
		
		public ArpeggioModel() {
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
			for (Parameters pattern : patch.getArpeggioPatterns()) {
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
		public void update(Observable source, Object detail) {
			if (source == patch) {
				if ("arpeggioCommon".equals(detail)) {
					addCommonObserver();
					fireTableStructureChanged();
				} else if ("arpeggioPatterns".equals(detail)) {
					addPatternObservers();
					fireTableDataChanged();
				}
			} else if (source == patch.getArpeggioCommon()) {
				fireTableStructureChanged();
			} else if (source instanceof ArpeggioPattern) {
				int row = 0;
				for (ArpeggioPattern pattern : patch.getArpeggioPatterns()) {
					if (source == pattern) {
						fireTableRowsUpdated(row, row);
						break;
					}
				}
				row++;
			}
		}
	}
	
}
