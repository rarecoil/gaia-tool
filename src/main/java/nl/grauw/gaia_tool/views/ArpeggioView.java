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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import nl.grauw.gaia_tool.Note;
import nl.grauw.gaia_tool.NoteValue;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.IntValue;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.ArpeggioCommon;
import nl.grauw.gaia_tool.parameters.ArpeggioPattern;
import nl.grauw.gaia_tool.views.parameters.ArpeggioCommonView;

public class ArpeggioView extends JPanel implements AWTObserver {
	private static final long serialVersionUID = 1L;
	
	private Patch patch;
	
	private JPanel arpeggioCommonContainer;
	private ArpeggioCommonView arpeggioCommonView;
	private JScrollPane patternScrollPane;
	private JTable patternTable;
	
	public ArpeggioView(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		initComponents();
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
		}
	}
	
	protected void initComponents() {
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
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
	
	private JPanel getArpeggioCommonContainer() {
		if (arpeggioCommonContainer == null) {
			arpeggioCommonContainer = new JPanel();
			arpeggioCommonContainer.setLayout(new BorderLayout());
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
			patternTable = new ArpeggioTable(new ArpeggioTableModel());
		}
		return patternTable;
	}
	
	private static class ArpeggioTable extends JTable {
		private static final long serialVersionUID = 1L;
		
		public ArpeggioTable(ArpeggioTableModel model) {
			super(model);
			setFillsViewportHeight(true);
			setDefaultEditor(Object.class, new CellEditor());
			getTableHeader().setToolTipText(
				"<html>" +
				"The note column specifies the “original note” of each pattern. This can be either<br>" +
				"a note in the notation “C#4” (note letter, optional sharp, and octave number), or<br>" +
				"OFF to disable the pattern. When a pattern is OFF, any patterns that follow will<br>" +
				"not be played either.<br>" +
				"<br>" +
				"For up/down and random motifs, the note specified does not influence the actual<br>" +
				"note played, but it does influence how patterns are matched up with the notes<br>" +
				"played by the user; e.g. C4 D4 E4 uses the first pattern for the lowest note and<br>" +
				"the third for the highest, whereas for E4 D4 C4 this is reversed. When the same<br>" +
				"notes are specified the row order is used, so to keep things simple you may just<br>" +
				"want to use the same original note for all patterns, e.g. C4 C4 C4.<br>" +
				"<br>" +
				"For the phrase motif, it specifies what note to play relative to the base note.<br>" +
				"The base note is the lowest note sounded on step 1.<br>" +
				"<br>" +
				"In the numbered columns you can specify for each step whether to play the note.<br>" +
				"Enter a number in the range 1-127 to play a note with the specified velocity.<br>" +
				"A 0 indicates a rest, and you can enter “Tie” to connect two steps together." +
				"</html>"
			);
		}
		
		private static class CellEditor extends DefaultCellEditor {
			private static final long serialVersionUID = 1L;
			
			public CellEditor() {
				super(new CellEditField());
			}
			
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
				// selects the text when starting to edit (by typing)
				JTextField editField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
				editField.selectAll();
				return editField;
			}
			
			private static class CellEditField extends JTextField {
				private static final long serialVersionUID = 1L;
				
				public CellEditField() {
					setBorder(BorderFactory.createLineBorder(Color.BLACK));
				}
				
				@Override
				public void requestFocus() {
					// selects the text on focus (by double-clicking)
					selectAll();
					super.requestFocus();
				}
				
			}
			
		}
		
	}
	
	public class ArpeggioTableModel extends AbstractTableModel implements AWTObserver {
		private static final long serialVersionUID = 1L;
		
		public ArpeggioTableModel() {
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
