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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import nl.grauw.gaia_tool.FilePatch;
import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.GaiaTool;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.UserPatch;
import nl.grauw.gaia_tool.Gaia.GaiaNotFoundException;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;

public class GaiaToolView extends JFrame implements TreeSelectionListener, AWTObserver {
	
	private GaiaTool gaiaTool;
	private Gaia gaia;
	
	private static final long serialVersionUID = 1L;
	private JScrollPane contentSelectionScrollPane;
	private ContentSelectionTree contentSelectionTree;
	private JPanel contentPanel;
	private LogView logView;

	public GaiaToolView(GaiaTool gaiaTool) {
		this.gaiaTool = gaiaTool;
		gaia = gaiaTool.getGaia();
		gaia.addObserver(this);
		
		initComponents();
		updateContentPanel();
	}
	
	public void exit() {
		dispose();
		gaiaTool.exit();
	}

	private void initComponents() {
		setTitle("Roland GAIA SH-01 tool — by Grauw");
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowCloseListener());
		GroupLayout layout = new GroupLayout(getContentPane());
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addGroup(
						layout.createSequentialGroup()
							.addComponent(getContentSelectionScrollPane(), GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
							.addComponent(getContentPanel())
					)
					.addComponent(getLogView())
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup()
							.addComponent(getContentSelectionScrollPane())
							.addComponent(getContentPanel())
					)
					.addComponent(getLogView(), GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
			);
		setJMenuBar(new MainMenuBar());
		setSize(900, 600);
	}

	private LogView getLogView() {
		if (logView == null) {
			logView = new LogView(gaiaTool.getLog());
		}
		return logView;
	}

	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new BorderLayout());
			contentPanel.add(getContentSelectionTree().getSelectedContentView());
		}
		return contentPanel;
	}

	private JScrollPane getContentSelectionScrollPane() {
		if (contentSelectionScrollPane == null) {
			contentSelectionScrollPane = new JScrollPane();
			contentSelectionScrollPane.setViewportView(getContentSelectionTree());
		}
		return contentSelectionScrollPane;
	}

	private ContentSelectionTree getContentSelectionTree() {
		if (contentSelectionTree == null) {
			contentSelectionTree = new ContentSelectionTree(gaiaTool);
			contentSelectionTree.addTreeSelectionListener(this);
		}
		return contentSelectionTree;
	}
	
	private void updateContentPanel() {
		JPanel cp = getContentPanel();
		JComponent spv = contentSelectionTree.getSelectedContentView();
		if (cp.getComponent(0) != spv) {
			cp.removeAll();
			cp.add(spv);
			cp.revalidate();
		}
	}
	
	@Override
	public void update(Observable source, Object detail) {
		if ("identityConfirmed".equals(detail)) {
			updateContentPanel();
		}
	}
	
	private void save() {
		Patch patch = contentSelectionTree.getSelectedPatch();
		if (patch == null) {
			JOptionPane.showMessageDialog(this, "You must select a patch to save.",
					"No patch selected.", JOptionPane.ERROR_MESSAGE);
		} else {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(gaiaTool.getCurrentDirectory());
			FileFilter filter = new FileNameExtensionFilter("GAIA patch file", "gaia");
			fc.addChoosableFileFilter(filter);
			fc.setFileFilter(filter);
			if (patch instanceof TemporaryPatch) {
				fc.setSelectedFile(new File(fc.getCurrentDirectory(), "patch-temporary.gaia"));
			} else if (patch instanceof UserPatch) {
				fc.setSelectedFile(new File(fc.getCurrentDirectory(), String.format("patch-%s-%d.gaia",
						"ABCDEFGH".charAt(((UserPatch)patch).getBank()), ((UserPatch)patch).getPatch() + 1)));
			} else if (patch instanceof FilePatch) {
				fc.setSelectedFile(((FilePatch)patch).getSource());
			}
			int result = fc.showSaveDialog(this);
			gaiaTool.setCurrentDirectory(fc.getCurrentDirectory());
			if (result == JFileChooser.APPROVE_OPTION) {
				gaiaTool.savePatch(fc.getSelectedFile(), contentSelectionTree.getSelectedPatch());
			}
		}
	}
	
	private void load() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(gaiaTool.getCurrentDirectory());
		FileFilter filter = new FileNameExtensionFilter("GAIA patch file", "gaia");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		int result = fc.showOpenDialog(this);
		gaiaTool.setCurrentDirectory(fc.getCurrentDirectory());
		if (result == JFileChooser.APPROVE_OPTION) {
			gaiaTool.loadGaiaPatch(fc.getSelectedFile(), gaia.getTemporaryPatch());
		}
	}
	
	private void reconnect() {
		try {
			gaia.close();
			gaia.open();
		} catch (GaiaNotFoundException e) {
			JOptionPane.showMessageDialog(this, "The GAIA MIDI ports could not be found. Is your GAIA turned on?",
					"Problem connecting to Roland GAIA", JOptionPane.ERROR_MESSAGE);
		} catch (MidiUnavailableException ex) {
			JOptionPane.showMessageDialog(this, "MIDI port unavailable",
					"Problem connecting to Roland GAIA", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (e.getSource() == contentSelectionTree) {
			updateContentPanel();
		}
	}
	
	private class WindowCloseListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			exit();
		}
	}
	
	private class MainMenuBar extends JMenuBar {
		private static final long serialVersionUID = 1L;
		
		public MainMenuBar() {
			add(new FileMenu());
			add(new TestMenu());
			add(new ToolsMenu());
		}
		
		private class FileMenu extends JMenu {
			private static final long serialVersionUID = 1L;
			
			public FileMenu() {
				super("File");
				add(new LoadItem());
				add(new SaveItem());
				if (!"Mac OS X".equals(System.getProperty("os.name"))) {
					addSeparator();
					add(new ExitItem());
				}
			}
			
			private class LoadItem extends JMenuItem implements ActionListener, AWTObserver {
				private static final long serialVersionUID = 1L;
				
				public LoadItem() {
					super("Load patch…");
					setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
							Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
					addActionListener(this);
					gaia.addObserver(this);
					update(null, "identityConfirmed");
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					load();
				}
				
				@Override
				public void update(Observable source, Object detail) {
					if ("identityConfirmed".equals(detail)) {
						setEnabled(gaia.isIdentityConfirmed());
					}
				}
			}
			
			private class SaveItem extends JMenuItem implements ActionListener, AWTObserver {
				private static final long serialVersionUID = 1L;
				
				public SaveItem() {
					super("Save patch as…");
					setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
							Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
					addActionListener(this);
					gaia.addObserver(this);
					update(null, "identityConfirmed");
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					save();
				}
				
				@Override
				public void update(Observable source, Object detail) {
					if ("identityConfirmed".equals(detail)) {
						setEnabled(gaia.isIdentityConfirmed());
					}
				}
			}
			
			private class ExitItem extends JMenuItem implements ActionListener {
				private static final long serialVersionUID = 1L;
				
				public ExitItem() {
					super("Exit");
					addActionListener(this);
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					exit();
				}
			}
		}
		
		private class TestMenu extends JMenu implements AWTObserver {
			private static final long serialVersionUID = 1L;
			
			public TestMenu() {
				super("Test");
				add(new PlayTestNotesItem());
				if (GaiaTool.DEBUG)
					add(new TestControlChange());
				gaia.addObserver(this);
				update(null, "opened");
			}
			
			@Override
			public void update(Observable source, Object detail) {
				if ("opened".equals(detail)) {
					setEnabled(gaia.isOpened());
				}
			}
			
			private class PlayTestNotesItem extends JMenuItem implements ActionListener {
				private static final long serialVersionUID = 1L;
				
				public PlayTestNotesItem() {
					super("Play test notes");
					addActionListener(this);
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					gaia.playTestNote();
					gaia.playGMTestNote();
				}
			}
			
			private class TestControlChange extends JMenuItem implements ActionListener {
				private static final long serialVersionUID = 1L;

				public TestControlChange() {
					super("Test control change");
					addActionListener(this);
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					gaia.testControlChange();
				}
			}
		}
		
		private class ToolsMenu extends JMenu {
			private static final long serialVersionUID = 1L;
			
			public ToolsMenu() {
				super("Tools");
				add(new ReconnectItem());
				add(new ConfigureMidiItem());
			}
			
			private class ReconnectItem extends JMenuItem implements ActionListener {
				private static final long serialVersionUID = 1L;
				
				public ReconnectItem() {
					super("Reconnect");
					addActionListener(this);
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					reconnect();
				}
			}
			
			private class ConfigureMidiItem extends JMenuItem implements ActionListener {
				private static final long serialVersionUID = 1L;
				
				public ConfigureMidiItem() {
					super("Configure MIDI…");
					addActionListener(this);
				}
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new MIDIDeviceSelector(gaia, this).show();
				}
			}
		}
	}
	
}
