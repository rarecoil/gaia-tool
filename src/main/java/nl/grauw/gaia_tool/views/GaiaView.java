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

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Patch;

public class GaiaView extends JFrame implements ActionListener, TreeSelectionListener, WindowListener {
	
	private Gaia gaia;
	
	private static final long serialVersionUID = 1L;
	private JMenuBar mainMenuBar;
	private JMenu fileMenu;
	private JMenuItem exitItem;
	private JMenu testMenu;
	private JMenuItem playTestNotesItem;
	private JMenuItem gmSystemOnItem;
	private JMenuItem gm2SystemOnItem;
	private JMenuItem gmSystemOffItem;
	private JScrollPane contentSelectionScrollPane;
	private JTree contentSelectionTree;
	private JPanel contentPanel;
	private LogView logView;
	private IntroPanel introPanel;

	public GaiaView() {
		initComponents();
	}
	
	public GaiaView(Gaia g) {
		this();
		setModel(g);
	}
	
	public void setModel(Gaia g) {
		gaia = g;
		logView.setModel(g.getLog());
		updateContentPanel();
	}

	public static void installLookAndFeel() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exit() {
		dispose();
		gaia.close();
		System.exit(0);
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
			logView = new LogView();
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

	private JMenuBar getMainMenuBar() {
		if (mainMenuBar == null) {
			mainMenuBar = new JMenuBar();
			mainMenuBar.add(getFileMenu());
			mainMenuBar.add(getTestMenu());
		}
		return mainMenuBar;
	}

	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setOpaque(false);
			fileMenu.add(getExitItem());
		}
		return fileMenu;
	}

	private JMenuItem getExitItem() {
		if (exitItem == null) {
			exitItem = new JMenuItem();
			exitItem.setText("Exit");
			exitItem.setOpaque(false);
			exitItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
			exitItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					exitItemActionPerformed(event);
				}
			});
		}
		return exitItem;
	}

	private JMenu getTestMenu() {
		if (testMenu == null) {
			testMenu = new JMenu();
			testMenu.setText("Test");
			testMenu.setOpaque(false);
			testMenu.add(getPlayTestNotesItem());
			testMenu.add(getGMSystemOnItem());
			testMenu.add(getGM2SystemOnItem());
			testMenu.add(getGMSystemOffItem());
		}
		return testMenu;
	}

	private JMenuItem getPlayTestNotesItem() {
		if (playTestNotesItem == null) {
			playTestNotesItem = new JMenuItem();
			playTestNotesItem.setText("Play test notes");
			playTestNotesItem.setOpaque(false);
			playTestNotesItem.addActionListener(this);
		}
		return playTestNotesItem;
	}

	private JMenuItem getGMSystemOnItem() {
		if (gmSystemOnItem == null) {
			gmSystemOnItem = new JMenuItem();
			gmSystemOnItem.setText("GM system on");
			gmSystemOnItem.setOpaque(false);
			gmSystemOnItem.addActionListener(this);
		}
		return gmSystemOnItem;
	}

	private JMenuItem getGM2SystemOnItem() {
		if (gm2SystemOnItem == null) {
			gm2SystemOnItem = new JMenuItem();
			gm2SystemOnItem.setText("GM2 system on");
			gm2SystemOnItem.setOpaque(false);
			gm2SystemOnItem.addActionListener(this);
		}
		return gm2SystemOnItem;
	}

	private JMenuItem getGMSystemOffItem() {
		if (gmSystemOffItem == null) {
			gmSystemOffItem = new JMenuItem();
			gmSystemOffItem.setText("GM system off");
			gmSystemOffItem.setOpaque(false);
			gmSystemOffItem.addActionListener(this);
		}
		return gmSystemOffItem;
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
		patchNode.add(new DefaultMutableTreeNode("Common"));
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
		if (spv != null && cp.getComponent(0) != spv) {
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
		} else if (tp.getPathCount() >= 2) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(1);
			if ("System".equals(node1.getUserObject()) && tp.getPathCount() == 2) {
				return new SystemView(gaia);
			} else if ("Temporary patch".equals(node1.getUserObject()) && tp.getPathCount() >= 3) {
				return getPatchParameterByName(gaia.getTemporaryPatch(), tp, 2);
			} else if ("User patches".equals(node1.getUserObject()) && tp.getPathCount() >= 5) {
				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)tp.getPathComponent(2);
				DefaultMutableTreeNode node3 = (DefaultMutableTreeNode)tp.getPathComponent(3);
				int bank = node1.getIndex(node2);
				int patch = node2.getIndex(node3);
				return getPatchParameterByName(gaia.getUserPatch(bank, patch), tp, 4);
			}
		}
		return new JPanel();
	}
	
	public JPanel getPatchParameterByName(Patch ppg, TreePath tp, int startIndex) {
		DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(startIndex);
		String desc = (String)node1.getUserObject();
		if ("Common".equals(desc)) {
			return new PatchCommonView(ppg);
		} else if ("Tone 1".equals(desc)) {
			return new ToneView(ppg, 1);
		} else if ("Tone 2".equals(desc)) {
			return new ToneView(ppg, 2);
		} else if ("Tone 3".equals(desc)) {
			return new ToneView(ppg, 3);
		} else if ("Distortion".equals(desc)) {
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
		throw new RuntimeException("Parameters not found.");
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == playTestNotesItem) {
				gaia.playTestNote();
				gaia.playGMTestNote();
			} else if (e.getSource() == gmSystemOnItem) {
				gaia.sendGM1SystemOn();
			} else if (e.getSource() == gm2SystemOnItem) {
				gaia.sendGM2SystemOn();
			} else if (e.getSource() == gmSystemOffItem) {
				gaia.sendGMSystemOff();
			}
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}
	}

	private void exitItemActionPerformed(ActionEvent event) {
		exit();
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
