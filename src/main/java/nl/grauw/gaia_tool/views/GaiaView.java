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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

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

import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.UserPatch;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;

public class GaiaView extends JFrame implements ActionListener, TreeSelectionListener, WindowListener, AWTObserver {
	
	private Gaia gaia;
	
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
	private JMenuItem configureMidiItem;
	private JScrollPane contentSelectionScrollPane;
	private JTree contentSelectionTree;
	private JPanel contentPanel;
	private LogView logView;
	private IntroPanel introPanel;
	private NotConnectedPanel notConnectedPanel;

	public GaiaView(Gaia gaia) {
		this.gaia = gaia;
		gaia.addObserver(this);
		
		initComponents();
		updateContentPanel();
	}
	
	public void exit() {
		dispose();
		gaia.exit();
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
			logView = new LogView(gaia.getLog());
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
			notConnectedPanel = new NotConnectedPanel(gaia);
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
			fileMenu.addSeparator();
			fileMenu.add(getExitItem());
		}
		return fileMenu;
	}

	private JMenuItem getExitItem() {
		if (exitItem == null) {
			exitItem = new JMenuItem("Exit");
			exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
			exitItem.addActionListener(this);
		}
		return exitItem;
	}

	private JMenuItem getLoadItem() {
		if (loadItem == null) {
			loadItem = new JMenuItem("Load Patch…");
			loadItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
			loadItem.addActionListener(this);
			getLoadItem().setEnabled(gaia.isIdentityConfirmed());
		}
		return loadItem;
	}

	private JMenuItem getSaveItem() {
		if (saveItem == null) {
			saveItem = new JMenuItem("Save Patch As…");
			saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
			saveItem.addActionListener(this);
			getSaveItem().setEnabled(gaia.isIdentityConfirmed());
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
			
			testMenu.setEnabled(gaia.isOpened());
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
			toolsMenu.add(getConfigureMIDIItem());
		}
		return toolsMenu;
	}

	private JMenuItem getConfigureMIDIItem() {
		if (configureMidiItem == null) {
			configureMidiItem = new JMenuItem("Configure MIDI");
			configureMidiItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					configureMIDIActionPerformed(event);
				}
			});
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
		} else if (!gaia.isOpened() || !gaia.isIdentityConfirmed()) {
			return getNotConnectedPanel();
		} else if (tp.getPathCount() >= 2) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(1);
			if ("System".equals(node1.getUserObject()) && tp.getPathCount() == 2) {
				return new SystemView(gaia);
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
	public Patch getSelectedPatch() {
		TreePath tp = contentSelectionTree.getSelectionPath();
		if (tp != null && tp.getPathCount() >= 2) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(1);
			if ("Temporary patch".equals(node1.getUserObject())) {
				return gaia.getTemporaryPatch();
			}
			if ("User patches".equals(node1.getUserObject()) && tp.getPathCount() >= 4) {
				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)tp.getPathComponent(2);
				DefaultMutableTreeNode node3 = (DefaultMutableTreeNode)tp.getPathComponent(3);
				int bank = node1.getIndex(node2);
				int patch = node2.getIndex(node3);
				return gaia.getUserPatch(bank, patch);
			}
		}
		return null;
	}
	
	public JPanel getPatchParameterByName(Patch ppg, TreePath tp, int startIndex) {
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
				return new DistortionPanel(ppg);
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
			getTestMenu().setEnabled(gaia.isOpened());
		} else if ("identityConfirmed".equals(detail)) {
			updateContentPanel();
			getLoadItem().setEnabled(gaia.isIdentityConfirmed());
			getSaveItem().setEnabled(gaia.isIdentityConfirmed());
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playTestNotesItem) {
			gaia.playTestNote();
			gaia.playGMTestNote();
		} else if (e.getSource() == gmSystemOnItem) {
			gaia.sendGM1SystemOn();
		} else if (e.getSource() == gm2SystemOnItem) {
			gaia.sendGM2SystemOn();
		} else if (e.getSource() == gmSystemOffItem) {
			gaia.sendGMSystemOff();
		} else if (e.getSource() == exitItem) {
			exit();
		} else if (e.getSource() == saveItem) {
			save();
		} else if (e.getSource() == loadItem) {
			load();
		} else if (e.getSource() == configureMidiItem) {
			new MIDIDeviceSelector(gaia, this).show();
		}
	}
	
	private void save() {
		Patch patch = getSelectedPatch();
		if (patch == null) {
			JOptionPane.showMessageDialog(this, "You must select a patch to save.",
					"No patch selected.", JOptionPane.ERROR_MESSAGE);
		} else {
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new FileNameExtensionFilter("GAIA patch file", "gaia"));
			if (patch instanceof TemporaryPatch) {
				fc.setSelectedFile(new File(fc.getCurrentDirectory(), "patch-temporary.gaia"));
			} else if (patch instanceof UserPatch) {
				fc.setSelectedFile(new File(fc.getCurrentDirectory(), String.format("patch-%s-%d.gaia",
						"ABCDEFGH".charAt(((UserPatch)patch).getBank()), ((UserPatch)patch).getPatch() + 1)));
			}
			int result = fc.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				gaia.savePatch(fc.getSelectedFile(), getSelectedPatch());
			}
		}
	}
	
	private void load() {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileNameExtensionFilter("GAIA patch file", "gaia"));
		int result = fc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			gaia.loadPatch(fc.getSelectedFile(), gaia.getTemporaryPatch());
		}
	}

	private void configureMIDIActionPerformed(ActionEvent event) {
		new MIDIDeviceSelector(gaia, this).show();
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
