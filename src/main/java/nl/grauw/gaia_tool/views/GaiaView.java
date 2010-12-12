package nl.grauw.gaia_tool.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
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

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

//VS4E -- DO NOT REMOVE THIS LINE!
public class GaiaView extends JFrame implements ActionListener, TreeSelectionListener, Observer {
	
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
	private ParametersView parametersView;

	private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";

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

	private void initComponents() {
		setTitle("Roland GAIA SH-01 tool — by Grauw");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new GroupLayout());
		add(getLogView(), new Constraints(new Bilateral(12, 12, 25), new Trailing(12, 97, 10, 10)));
		add(getContentPanel(), new Constraints(new Bilateral(219, 12, 0), new Bilateral(11, 121, 0)));
		add(contentSelectionScrollPane(), new Constraints(new Leading(12, 195, 18, 18), new Bilateral(11, 121, 25)));
		setJMenuBar(getMainMenuBar());
		setSize(863, 557);
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

	private JScrollPane contentSelectionScrollPane() {
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

	public static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	private void exitItemActionPerformed(ActionEvent event) {
		dispose();
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

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (e.getSource() == contentSelectionTree) {
			updateContentPanel();
		}
	}
	
	public void updateContentPanel() {
		Parameters p = getSelectedParameters();
		parametersView.setModel(p);
	}
	
	public Parameters getSelectedParameters() {
		TreePath tp = contentSelectionTree.getSelectionPath();
		if (tp != null && tp.getPathCount() >= 2) {
			DefaultMutableTreeNode node1 = (DefaultMutableTreeNode)tp.getPathComponent(1);
			if ("System".equals(node1.getUserObject())) {
				Parameters p = gaia.getSystem();
				if (p == null) {
					try {
						gaia.sendSystemDataRequest();
					} catch(InvalidMidiDataException ex) {
						ex.printStackTrace();
					}
				}
				return p;
			} else if ("Temporary patch".equals(node1.getUserObject()) && tp.getPathCount() == 3) {
				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)tp.getPathComponent(2);
				Parameters p = getPatchParameterByName(gaia.getTemporaryPatch(), (String)node2.getUserObject());
				if (p == null) {
					try {
						gaia.sendTemporaryPatchDataRequest();
					} catch(InvalidMidiDataException ex) {
						ex.printStackTrace();
					}
				}
				return p;
			} else if ("User patches".equals(node1.getUserObject()) && tp.getPathCount() == 5) {
				DefaultMutableTreeNode node2 = (DefaultMutableTreeNode)tp.getPathComponent(2);
				DefaultMutableTreeNode node3 = (DefaultMutableTreeNode)tp.getPathComponent(3);
				DefaultMutableTreeNode node4 = (DefaultMutableTreeNode)tp.getPathComponent(4);
				int bank = node1.getIndex(node2);
				int patch = node2.getIndex(node3);
				Parameters p = getPatchParameterByName(gaia.getUserPatch(bank, patch), (String)node4.getUserObject());
				if (p == null) {
					try {
						gaia.sendUserPatchDataRequest(bank, patch);
					} catch(InvalidMidiDataException ex) {
						ex.printStackTrace();
					}
				}
				return p;
			}
		}
		return null;
	}
	
	public Parameters getPatchParameterByName(PatchParameterGroup ppg, String desc) {
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
			return ppg.getArpeggioCommon();
		}
		throw new RuntimeException("Parameters not found.");
	}
	
}
