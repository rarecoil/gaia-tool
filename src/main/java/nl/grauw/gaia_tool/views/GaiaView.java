package nl.grauw.gaia_tool.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import nl.grauw.gaia_tool.Gaia;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

//VS4E -- DO NOT REMOVE THIS LINE!
public class GaiaView extends JFrame implements ActionListener {
	
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
	private JMenu dataRequestMenu;
	private JMenuItem systemDataItem;
	private JMenuItem temporaryPatchDataItem;
	private JScrollPane jScrollPane1;
	private JTree jTree0;
	private JPanel jPanel0;
	private LogView logView;

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
	}

	private void initComponents() {
		setTitle("Roland GAIA SH-01 tool — by Grauw");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new GroupLayout());
		add(getLogView(), new Constraints(new Bilateral(12, 12, 25), new Trailing(12, 97, 10, 10)));
		add(getJPanel0(), new Constraints(new Bilateral(219, 12, 0), new Bilateral(11, 121, 0)));
		add(getJScrollPane1(), new Constraints(new Leading(12, 195, 18, 18), new Bilateral(11, 121, 25)));
		setJMenuBar(getMainMenuBar());
		setSize(863, 557);
	}

	private LogView getLogView() {
		if (logView == null) {
			logView = new LogView();
		}
		return logView;
	}

	private JPanel getJPanel0() {
		if (jPanel0 == null) {
			jPanel0 = new JPanel();
			jPanel0.setLayout(new GroupLayout());
		}
		return jPanel0;
	}

	private JMenuBar getMainMenuBar() {
		if (mainMenuBar == null) {
			mainMenuBar = new JMenuBar();
			mainMenuBar.add(getFileMenu());
			mainMenuBar.add(getTestMenu());
			mainMenuBar.add(getDataRequestMenu());
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

	private JMenu getDataRequestMenu() {
		if (dataRequestMenu == null) {
			dataRequestMenu = new JMenu();
			dataRequestMenu.setText("Data request");
			dataRequestMenu.setOpaque(false);
			dataRequestMenu.add(getSystemDataItem());
			dataRequestMenu.add(getTemporaryPatchDataItem());
		}
		return dataRequestMenu;
	}

	private JMenuItem getSystemDataItem() {
		if (systemDataItem == null) {
			systemDataItem = new JMenuItem();
			systemDataItem.setText("System data");
			systemDataItem.setOpaque(false);
			systemDataItem.addActionListener(this);
		}
		return systemDataItem;
	}

	private JMenuItem getTemporaryPatchDataItem() {
		if (temporaryPatchDataItem == null) {
			temporaryPatchDataItem = new JMenuItem();
			temporaryPatchDataItem.setText("Temporary patch data");
			temporaryPatchDataItem.setOpaque(false);
			temporaryPatchDataItem.addActionListener(this);
		}
		return temporaryPatchDataItem;
	}

	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJTree0());
		}
		return jScrollPane1;
	}

	private JTree getJTree0() {
		if (jTree0 == null) {
			jTree0 = new JTree();
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
			jTree0.setModel(treeModel);
			jTree0.setShowsRootHandles(true);
			jTree0.setRootVisible(false);
		}
		return jTree0;
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
			} else if (e.getSource() == systemDataItem) {
				gaia.sendSystemDataRequest();
			} else if (e.getSource() == temporaryPatchDataItem) {
				gaia.sendPatchDataRequest();
			}
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}
	}

}
