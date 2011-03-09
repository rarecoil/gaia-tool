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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nl.grauw.gaia_tool.GaiaTool;
import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.UserPatch;
import nl.grauw.gaia_tool.Gaia.GaiaNotFoundException;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;

public class GaiaToolView extends JFrame implements ActionListener, TreeSelectionListener, WindowListener, AWTObserver {
	
	private GaiaTool gaiaTool;
	
	private static final long serialVersionUID = 1L;
	private JMenuBar mainMenuBar;
	private JMenu fileMenu;
	private JMenuItem loadItem;
	private JMenuItem saveItem;
	private JMenuItem exitItem;
	private JMenu testMenu;
	private JMenuItem playTestNotesItem;
	private JMenuItem gmSystemOnItem;
	private JMenuItem gm2SystemOnItem;
	private JMenuItem gmSystemOffItem;
	private JMenu toolsMenu;
	private JMenuItem reconnectItem;
	private JMenuItem configureMidiItem;
	private JScrollPane contentSelectionScrollPane;
	private JTree contentSelectionTree;
	private JPanel contentPanel;
	private LogView logView;
	private IntroPanel introPanel;
	private NotConnectedPanel notConnectedPanel;

	public GaiaToolView(GaiaTool gaiaTool) {
		this.gaiaTool = gaiaTool;
		gaiaTool.getGaia().addObserver(this);
		
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
		addWindowListener(this);
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
		setJMenuBar(getMainMenuBar());
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
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
			contentPanel.add(getIntroPanel());
		}
		return contentPanel;
	}

	private IntroPanel getIntroPanel() {
		if (introPanel == null) {
			introPanel = new IntroPanel();
		}
		return introPanel;
	}

	private NotConnectedPanel getNotConnectedPanel() {
		if (notConnectedPanel == null) {
			notConnectedPanel = new NotConnectedPanel(gaiaTool.getGaia());
		}
		return notConnectedPanel;
	}

	private JMenuBar getMainMenuBar() {
		if (mainMenuBar == null) {
			mainMenuBar = new JMenuBar();
			mainMenuBar.add(getFileMenu());
			mainMenuBar.add(getTestMenu());
			mainMenuBar.add(getToolsMenu());
		}
		return mainMenuBar;
	}

	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu("File");
			fileMenu.add(getLoadItem());
			fileMenu.add(getSaveItem());
			if (!"Mac OS X".equals(System.getProperty("os.name"))) {
				fileMenu.addSeparator();
				fileMenu.add(getExitItem());
			}
		}
		return fileMenu;
	}

	private JMenuItem getExitItem() {
		if (exitItem == null) {
			exitItem = new JMenuItem("Exit");
			exitItem.addActionListener(this);
		}
		return exitItem;
	}

	private JMenuItem getLoadItem() {
		if (loadItem == null) {
			loadItem = new JMenuItem("Load Patch…");
			loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			loadItem.addActionListener(this);
			getLoadItem().setEnabled(gaiaTool.getGaia().isIdentityConfirmed());
		}
		return loadItem;
	}

	private JMenuItem getSaveItem() {
		if (saveItem == null) {
			saveItem = new JMenuItem("Save Patch As…");
			saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			saveItem.addActionListener(this);
			getSaveItem().setEnabled(gaiaTool.getGaia().isIdentityConfirmed());
		}
		return saveItem;
	}

	private JMenu getTestMenu() {
		if (testMenu == null) {
			testMenu = new JMenu("Test");
			testMenu.add(getPlayTestNotesItem());
			testMenu.add(getGMSystemOnItem());
			testMenu.add(getGM2SystemOnItem());
			testMenu.add(getGMSystemOffItem());
			
			testMenu.setEnabled(gaiaTool.getGaia().isOpened());
		}
		return testMenu;
	}

	private JMenuItem getPlayTestNotesItem() {
		if (playTestNotesItem == null) {
			playTestNotesItem = new JMenuItem("Play Test Notes");
			playTestNotesItem.addActionListener(this);
		}
		return playTestNotesItem;
	}

	private JMenuItem getGMSystemOnItem() {
		if (gmSystemOnItem == null) {
			gmSystemOnItem = new JMenuItem("GM System On");
			gmSystemOnItem.addActionListener(this);
		}
		return gmSystemOnItem;
	}

	private JMenuItem getGM2SystemOnItem() {
		if (gm2SystemOnItem == null) {
			gm2SystemOnItem = new JMenuItem("GM2 System On");
			gm2SystemOnItem.addActionListener(this);
		}
		return gm2SystemOnItem;
	}

	private JMenuItem getGMSystemOffItem() {
		if (gmSystemOffItem == null) {
			gmSystemOffItem = new JMenuItem("GM System Off");
			gmSystemOffItem.addActionListener(this);
		}
		return gmSystemOffItem;
	}

	private JMenu getToolsMenu() {
		if (toolsMenu == null) {
			toolsMenu = new JMenu("Tools");
			toolsMenu.add(getReconnectItem());
			toolsMenu.add(getConfigureMidiItem());
		}
		return toolsMenu;
	}

	private JMenuItem getReconnectItem() {
		if (reconnectItem == null) {
			reconnectItem = new JMenuItem("Reconnect");
			reconnectItem.addActionListener(this);
		}
		return reconnectItem;
	}

	private JMenuItem getConfigureMidiItem() {
		if (configureMidiItem == null) {
			configureMidiItem = new JMenuItem("Configure MIDI");
			configureMidiItem.addActionListener(this);
		}
		return configureMidiItem;
	}

	private JScrollPane getContentSelectionScrollPane() {
		if (contentSelectionScrollPane == null) {
			contentSelectionScrollPane = new JScrollPane();
			contentSelectionScrollPane.setViewportView(getContentSelectionTree());
		}
		return contentSelectionScrollPane;
	}

	private JTree getContentSelectionTree() {
		if (contentSelectionTree == null) {
			contentSelectionTree = new JTree();
			DefaultTreeModel treeModel = createContentSelectionTreeModel();
			contentSelectionTree.setModel(treeModel);
			contentSelectionTree.setShowsRootHandles(true);
			contentSelectionTree.setRootVisible(false);
			contentSelectionTree.addTreeSelectionListener(this);
			contentSelectionTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		return contentSelectionTree;
	}
	
	private DefaultTreeModel createContentSelectionTreeModel() {
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Parameters");
		DefaultMutableTreeNode systemNode = new DefaultMutableTreeNode("System");
		rootNode.add(systemNode);
		DefaultMutableTreeNode temporaryPatchNode = new DefaultMutableTreeNode("Temporary patch");
		addPatchTreeNodesTo(temporaryPatchNode);
		rootNode.add(temporaryPatchNode);
		DefaultMutableTreeNode userPatchesNode = new DefaultMutableTreeNode("User patches");
		String[] banks = {"A", "B", "C", "D", "E", "F", "G", "H"};
		for (String bank : banks) {
			DefaultMutableTreeNode bankNode = new DefaultMutableTreeNode("Bank " + bank);
			String[] patches = {"1", "2", "3", "4", "5", "6", "7", "8"};
			for (String patch : patches) {
				DefaultMutableTreeNode patchNode = new DefaultMutableTreeNode("Patch " + bank + "-" + patch);
				addPatchTreeNodesTo(patchNode);
				bankNode.add(patchNode);
			}
			userPatchesNode.add(bankNode);
		}
		rootNode.add(userPatchesNode);
		return new DefaultTreeModel(rootNode);
	}
	
	private void addPatchTreeNodesTo(DefaultMutableTreeNode patchNode) {
		patchNode.add(new DefaultMutableTreeNode("Tone 1"));
		patchNode.add(new DefaultMutableTreeNode("Tone 2"));
		patchNode.add(new DefaultMutableTreeNode("Tone 3"));
		patchNode.add(new DefaultMutableTreeNode("Distortion"));
		patchNode.add(new DefaultMutableTreeNode("Flanger"));
		patchNode.add(new DefaultMutableTreeNode("Delay"));
		patchNode.add(new DefaultMutableTreeNode("Reverb"));
		patchNode.add(new DefaultMutableTreeNode("Arpeggio"));
	}
	
	private void updateContentPanel() {
		JPanel cp = getContentPanel();
		JPanel spv = getSelectedParametersView();
		if (cp.getComponent(0) != spv) {
			cp.removeAll();
			cp.add(spv);
			cp.revalidate();
		}
	}
	
	/**
	 * Returns a panel corresponding to the current selection in the tree.
	 * @return A panel that is to be shown, or null if the current panel should
	 *         hold off updating for a little longer (to avoid flickering).
	 */
	public JPanel getSelectedParametersView() {
		TreePath tp = contentSelectionTree.getSelectionPath();
		if (tp == null) {
			return getIntroPanel();
		} else if (!gaiaTool.getGaia().isOpened() || !gaiaTool.getGaia().isIdentityConfirmed()) {
			return getNotConnectedPanel();
		} else if (tp.getPathCount() >= 2) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(1);
			if ("System".equals(node1.getUserObject()) && tp.getPathCount() == 2) {
				return new SystemView(gaiaTool.getGaia());
			} else if ("Temporary patch".equals(node1.getUserObject()) && tp.getPathCount() >= 2) {
				return getPatchParameterByName(getSelectedPatch(), tp, 2);
			} else if ("User patches".equals(node1.getUserObject()) && tp.getPathCount() >= 4) {
				return getPatchParameterByName(getSelectedPatch(), tp, 4);
			}
		}
		return new JPanel();
	}
	
	/**
	 * Return the patch that is currently selected in the tree.
	 * @return
	 */
	public GaiaPatch getSelectedPatch() {
		TreePath tp = contentSelectionTree.getSelectionPath();
		if (tp != null && tp.getPathCount() >= 2) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(1);
			if ("Temporary patch".equals(node1.getUserObject())) {
				return gaiaTool.getGaia().getTemporaryPatch();
			}
			if ("User patches".equals(node1.getUserObject()) && tp.getPathCount() >= 4) {
				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)tp.getPathComponent(2);
				DefaultMutableTreeNode node3 = (DefaultMutableTreeNode)tp.getPathComponent(3);
				int bank = node1.getIndex(node2);
				int patch = node2.getIndex(node3);
				return gaiaTool.getGaia().getUserPatch(bank, patch);
			}
		}
		return null;
	}
	
	public JPanel getPatchParameterByName(GaiaPatch ppg, TreePath tp, int startIndex) {
		if (tp.getPathCount() == startIndex) {
			return new PatchView(ppg);
		} else if (tp.getPathCount() > startIndex) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(startIndex);
			String desc = (String)node1.getUserObject();
			if ("Tone 1".equals(desc)) {
				return new ToneView(ppg, 1);
			} else if ("Tone 2".equals(desc)) {
				return new ToneView(ppg, 2);
			} else if ("Tone 3".equals(desc)) {
				return new ToneView(ppg, 3);
			} else if ("Distortion".equals(desc)) {
//				return new DistortionPanel(ppg);
				return new DistortionView(ppg);
			} else if ("Flanger".equals(desc)) {
				return new FlangerView(ppg);
			} else if ("Delay".equals(desc)) {
				return new DelayView(ppg);
			} else if ("Reverb".equals(desc)) {
				return new ReverbView(ppg);
			} else if ("Arpeggio".equals(desc)) {
				return new ArpeggioView(ppg);
			}
		}
		throw new RuntimeException("Parameters not found.");
	}

	@Override
	public void update(Observable source, Object detail) {
		if ("opened".equals(detail)) {
			getTestMenu().setEnabled(gaiaTool.getGaia().isOpened());
		} else if ("identityConfirmed".equals(detail)) {
			updateContentPanel();
			getLoadItem().setEnabled(gaiaTool.getGaia().isIdentityConfirmed());
			getSaveItem().setEnabled(gaiaTool.getGaia().isIdentityConfirmed());
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playTestNotesItem) {
			gaiaTool.getGaia().playTestNote();
			gaiaTool.getGaia().playGMTestNote();
		} else if (e.getSource() == gmSystemOnItem) {
			gaiaTool.getGaia().sendGM1SystemOn();
		} else if (e.getSource() == gm2SystemOnItem) {
			gaiaTool.getGaia().sendGM2SystemOn();
		} else if (e.getSource() == gmSystemOffItem) {
			gaiaTool.getGaia().sendGMSystemOff();
		} else if (e.getSource() == exitItem) {
			exit();
		} else if (e.getSource() == saveItem) {
			save();
		} else if (e.getSource() == loadItem) {
			load();
		} else if (e.getSource() == reconnectItem) {
			reconnect();
		} else if (e.getSource() == configureMidiItem) {
			new MIDIDeviceSelector(gaiaTool.getGaia(), this).show();
		}
	}
	
	private void save() {
		GaiaPatch patch = getSelectedPatch();
		if (patch == null) {
			JOptionPane.showMessageDialog(this, "You must select a patch to save.",
					"No patch selected.", JOptionPane.ERROR_MESSAGE);
		} else {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(gaiaTool.getCurrentDirectory());
			fc.addChoosableFileFilter(new FileNameExtensionFilter("GAIA patch file", "gaia"));
			if (patch instanceof TemporaryPatch) {
				fc.setSelectedFile(new File(fc.getCurrentDirectory(), "patch-temporary.gaia"));
			} else if (patch instanceof UserPatch) {
				fc.setSelectedFile(new File(fc.getCurrentDirectory(), String.format("patch-%s-%d.gaia",
						"ABCDEFGH".charAt(((UserPatch)patch).getBank()), ((UserPatch)patch).getPatch() + 1)));
			}
			int result = fc.showSaveDialog(this);
			gaiaTool.setCurrentDirectory(fc.getCurrentDirectory());
			if (result == JFileChooser.APPROVE_OPTION) {
				gaiaTool.savePatch(fc.getSelectedFile(), getSelectedPatch());
			}
		}
	}
	
	private void load() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(gaiaTool.getCurrentDirectory());
		fc.addChoosableFileFilter(new FileNameExtensionFilter("GAIA patch file", "gaia"));
		int result = fc.showOpenDialog(this);
		gaiaTool.setCurrentDirectory(fc.getCurrentDirectory());
		if (result == JFileChooser.APPROVE_OPTION) {
			gaiaTool.loadPatch(fc.getSelectedFile(), gaiaTool.getGaia().getTemporaryPatch());
		}
	}
	
	private void reconnect() {
		try {
			gaiaTool.getGaia().close();
			gaiaTool.getGaia().open();
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

	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		exit();
	}
	
}