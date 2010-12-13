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
import nl.grauw.gaia_tool.PatchParameterGroup;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.mvc.Observer;
import nl.grauw.gaia_tool.parameters.Parameters;

public class GaiaView extends JFrame implements ActionListener, TreeSelectionListener, Observer, WindowListener {
	
	private Gaia gaia;
	
	private String lastDataRequest;

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
	private ParametersView parametersView;

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
		gaia.addObserver(this);
		// also observe temporary patch and user patches
		gaia.getTemporaryPatch().addObserver(this);
		for (int bank = 0; bank < 8; bank++) {
			for (int patch = 0; patch < 8; patch++) {
				gaia.getUserPatch(bank, patch).addObserver(this);
			}
		}
		update(g, null);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == gaia || o instanceof PatchParameterGroup) {
			updateContentPanel();
		}
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

	private ParametersView getParametersView() {
		if (parametersView == null) {
			parametersView = new ParametersView();
		}
		return parametersView;
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
			contentPanel.add(getParametersView());
		}
		return contentPanel;
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
			DefaultTreeModel treeModel = null;
			{
				DefaultMutableTreeNode node0 = new DefaultMutableTreeNode("Parameters");
				{
					DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("System");
					node0.add(node1);
				}
				{
					DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Temporary patch");
					addPatchTreeNodesTo(node1);
					node0.add(node1);
				}
				{
					DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("User patches");
					addUserPatchTreeNodesTo(node1);
					node0.add(node1);
				}
				treeModel = new DefaultTreeModel(node0);
			}
			contentSelectionTree.setModel(treeModel);
			contentSelectionTree.setShowsRootHandles(true);
			contentSelectionTree.setRootVisible(false);
			contentSelectionTree.addTreeSelectionListener(this);
			contentSelectionTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}
		return contentSelectionTree;
	}
	
	private void addPatchTreeNodesTo(DefaultMutableTreeNode node1) {
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Common");
			node1.add(node2);
		}
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Tone 1");
			node1.add(node2);
		}
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Tone 2");
			node1.add(node2);
		}
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Tone 3");
			node1.add(node2);
		}
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Distortion");
			node1.add(node2);
		}
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Flanger");
			node1.add(node2);
		}
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Delay");
			node1.add(node2);
		}
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Reverb");
			node1.add(node2);
		}
		{
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Arpeggio");
			{
				String[] patterns = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
				for (String pattern : patterns) {
					DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("Pattern " + pattern);
					node2.add(node3);
				}
			}
			node1.add(node2);
		}
	}
	
	private void addUserPatchTreeNodesTo(DefaultMutableTreeNode node1) {
		String[] banks = {"A", "B", "C", "D", "E", "F", "G", "H"};
		for (String bank : banks) {
			DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Bank " + bank);
			String[] patches = {"1", "2", "3", "4", "5", "6", "7", "8"};
			for (String patch : patches) {
				DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("Patch " + bank + "-" + patch);
				addPatchTreeNodesTo(node3);
				node2.add(node3);
			}
			node1.add(node2);
		}
	}
	
	public void updateContentPanel() {
		Parameters p = getSelectedParameters();
		parametersView.setModel(p);
	}
	
	public Parameters getSelectedParameters() {
		TreePath tp = contentSelectionTree.getSelectionPath();
		Parameters p = null;
		if (tp != null && tp.getPathCount() >= 2) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(1);
			if ("System".equals(node1.getUserObject()) && tp.getPathCount() == 2) {
				p = gaia.getSystem();
				if (p == null && !"system".equals(lastDataRequest)) {
					try {
						gaia.sendSystemDataRequest();
						lastDataRequest = "system";
					} catch(InvalidMidiDataException ex) {
						ex.printStackTrace();
					}
				}
			} else if ("Temporary patch".equals(node1.getUserObject()) && tp.getPathCount() >= 3) {
				p = getPatchParameterByName(gaia.getTemporaryPatch(), tp, 2);
				if (p == null && !"temporaryPatch".equals(lastDataRequest)) {
					try {
						gaia.sendTemporaryPatchDataRequest();
						lastDataRequest = "temporaryPatch";
					} catch(InvalidMidiDataException ex) {
						ex.printStackTrace();
					}
				}
			} else if ("User patches".equals(node1.getUserObject()) && tp.getPathCount() >= 5) {
				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)tp.getPathComponent(2);
				DefaultMutableTreeNode node3 = (DefaultMutableTreeNode)tp.getPathComponent(3);
				int bank = node1.getIndex(node2);
				int patch = node2.getIndex(node3);
				p = getPatchParameterByName(gaia.getUserPatch(bank, patch), tp, 4);
				if (p == null && !("userPatch" + bank + patch).equals(lastDataRequest)) {
					try {
						gaia.sendUserPatchDataRequest(bank, patch);
						lastDataRequest = "userPatch" + bank + patch;
					} catch(InvalidMidiDataException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return p;
	}
	
	public Parameters getPatchParameterByName(PatchParameterGroup ppg, TreePath tp, int startIndex) {
		DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(startIndex);
		String desc = (String)node1.getUserObject();
		if ("Common".equals(desc)) {
			return ppg.getCommon();
		} else if ("Tone 1".equals(desc)) {
			return ppg.getTone(1);
		} else if ("Tone 2".equals(desc)) {
			return ppg.getTone(2);
		} else if ("Tone 3".equals(desc)) {
			return ppg.getTone(3);
		} else if ("Distortion".equals(desc)) {
			return ppg.getDistortion();
		} else if ("Flanger".equals(desc)) {
			return ppg.getFlanger();
		} else if ("Delay".equals(desc)) {
			return ppg.getDelay();
		} else if ("Reverb".equals(desc)) {
			return ppg.getReverb();
		} else if ("Arpeggio".equals(desc)) {
			if (tp.getPathCount() == startIndex + 2) {
				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)tp.getPathComponent(startIndex + 1);
				return ppg.getArpeggioPattern(node1.getIndex(node2) + 1);
			}
			return ppg.getArpeggioCommon();
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
