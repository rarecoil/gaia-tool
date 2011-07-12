package nl.grauw.gaia_tool.views;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nl.grauw.gaia_tool.FilePatch;
import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.GaiaTool;
import nl.grauw.gaia_tool.Library;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.Patch.IncompletePatchException;
import nl.grauw.gaia_tool.PatchDataRequester;
import nl.grauw.gaia_tool.PatchDataRequester.PatchCompleteListener;
import nl.grauw.gaia_tool.TemporaryPatch;

public class ContentSelectionTree extends JTree {
	private static final long serialVersionUID = 1L;
	
	GaiaTool gaiaTool;
	
	public ContentSelectionTree(GaiaTool gaiaTool) {
		this.gaiaTool = gaiaTool;
		
		DefaultTreeModel treeModel = createContentSelectionTreeModel();
		setModel(treeModel);
		setShowsRootHandles(true);
		setRootVisible(false);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		addMouseListener(new PopupListener());
	}
	
	private DefaultTreeModel createContentSelectionTreeModel() {
		ContentSelectionTreeNode rootNode = new ContentSelectionTreeNode("Parameters");
		SystemTreeNode systemNode = new SystemTreeNode();
		rootNode.add(systemNode);
		PatchTreeNode temporaryPatchNode = new PatchTreeNode(gaiaTool.getGaia().getTemporaryPatch(), "Temporary patch");
		rootNode.add(temporaryPatchNode);
		ContentSelectionTreeNode userPatchesNode = createUserPatchesNode();
		rootNode.add(userPatchesNode);
		LibraryNode libraryNode = new LibraryNode(gaiaTool.getLibrary(), "Library");
		rootNode.add(libraryNode);
		return new DefaultTreeModel(rootNode);
	}
	
	private ContentSelectionTreeNode createUserPatchesNode() {
		ContentSelectionTreeNode userPatchesNode = new ContentSelectionTreeNode("User patches");
		for (int bank = 0; bank < 8; bank++) {
			ContentSelectionTreeNode bankNode = new ContentSelectionTreeNode("Bank " + "ABCDEFGH".charAt(bank));
			for (int patch = 0; patch < 8; patch++) {
				String patchName = "Patch " + "ABCDEFGH".charAt(bank) + "-" + (patch + 1);
				PatchTreeNode patchNode = new PatchTreeNode(gaiaTool.getGaia().getUserPatch(bank, patch), patchName);
				bankNode.add(patchNode);
			}
			userPatchesNode.add(bankNode);
		}
		return userPatchesNode;
	}
	
	/**
	 * Returns a panel corresponding to the current selection in the tree.
	 * @return A panel that is to be shown, or null if the current panel should
	 *         hold off updating for a little longer (to avoid flickering).
	 */
	public JComponent getSelectedContentView() {
		TreePath treePath = getSelectionPath();
		if (treePath == null)
			return new IntroPanel();
		
		ContentSelectionTreeNode selectedNode = (ContentSelectionTreeNode)treePath.getLastPathComponent();
		return selectedNode.getContentView();
	}
	
	/**
	 * Return the patch that is currently selected in the tree.
	 * @return The currently selected patch, or null.
	 */
	public Patch getSelectedPatch() {
		TreePath treePath = getSelectionPath();
		if (treePath != null) {
			for (int i = treePath.getPathCount() - 1; i >= 0; i--) {
				if (treePath.getPathComponent(i) instanceof PatchTreeNode) {
					return ((PatchTreeNode)treePath.getPathComponent(i)).getPatch();
				}
			}
		}
		return null;
	}
	
	public class PopupListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			showContextMenu(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			showContextMenu(e);
		}
		
		private void showContextMenu(MouseEvent e) {
			if (e.isPopupTrigger()) {
				TreePath treePath = getPathForLocation(e.getX(), e.getY());
				if (treePath != null && treePath.getLastPathComponent() instanceof ContentSelectionTreeNode) {
					ContentSelectionTreeNode node = (ContentSelectionTreeNode)treePath.getLastPathComponent();
					JPopupMenu contextMenu = node.getContextMenu();
					ContentSelectionTree.this.setSelectionPath(new TreePath(node.getPath()));
					if (contextMenu != null) {
						contextMenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		}
	}
	
	public class ContentSelectionTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 1L;
		
		public ContentSelectionTreeNode() {
			super();
		}
		
		public ContentSelectionTreeNode(String label) {
			super(label);
		}
		
		/**
		 * The content view to show when the node is selected.
		 * Intended to be overridden by subclasses.
		 * @return View to show when selected.
		 */
		public JComponent getContentView() {
			return new JPanel();
		}
		
		public JPopupMenu getContextMenu() {
			return null;
		}
	}
	
	public class LibraryNode extends ContentSelectionTreeNode {
		private static final long serialVersionUID = 1L;
		
		public LibraryNode(Library library) {
			this(library, library.getName());
		}
		
		public LibraryNode(Library library, String name) {
			super(name);
			for (Library sublibrary : library.getLibraries()) {
				add(new LibraryNode(sublibrary));
			}
			for (FilePatch patch : library.getPatches()) {
				add(new PatchTreeNode(patch, patch.getName()));
			}
		}
		
		@Override
		public JComponent getContentView() {
			return new LibraryPanel(gaiaTool);
		}
	}
	
	public class SystemTreeNode extends ContentSelectionTreeNode {
		private static final long serialVersionUID = 1L;
		
		public String toString() {
			return "System";
		}
		
		@Override
		public JComponent getContentView() {
			if (!gaiaTool.getGaia().isConnected())
				return new NotConnectedPanel(gaiaTool.getGaia());
			
			return new SystemView(gaiaTool.getGaia());
		}
	}
	
	public class PatchTreeNode extends ContentSelectionTreeNode {
		private static final long serialVersionUID = 1L;
		
		String name;
		Patch patch;
		JPopupMenu contextMenu;
		
		public PatchTreeNode(Patch patch, String name) {
			this.name = name;
			this.patch = patch;
			
			add(new ToneTreeNode(1));
			add(new ToneTreeNode(2));
			add(new ToneTreeNode(3));
			add(new DistortionTreeNode());
			add(new FlangerTreeNode());
			add(new DelayTreeNode());
			add(new ReverbTreeNode());
			add(new ArpeggioTreeNode());
		}
		
		public Patch getPatch() {
			return patch;
		}
		
		public String toString() {
			return name;
		}
		
		@Override
		public JComponent getContentView() {
			if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
				return new NotConnectedPanel(gaiaTool.getGaia());
			
			return new PatchView(patch);
		}
		
		public JPopupMenu getContextMenu() {
			if (contextMenu == null) {
				contextMenu = new PatchContextMenu();
			}
			return contextMenu;
		}
		
		public class PatchContextMenu extends JPopupMenu implements ActionListener {
			private static final long serialVersionUID = 1L;
			
			JMenuItem copy;
			
			public PatchContextMenu() {
				copy = new JMenuItem("Copy to temporary patch");
				copy.addActionListener(this);
				add(copy);
				update();
			}
			
			public void update() {
				copy.setEnabled(!(patch instanceof TemporaryPatch) && gaiaTool.getGaia().isConnected());
			}
			
			@Override
			public void show(Component invoker, int x, int y) {
				update();
				super.show(invoker, x, y);
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == copy) {
					copyToTemporaryPatch();
				}
			}
			
			public void copyToTemporaryPatch() {
				if (patch instanceof TemporaryPatch || !gaiaTool.getGaia().isConnected())
					throw new RuntimeException("Preconditions not satisfied.");
				
				TemporaryPatch temporaryPatch = gaiaTool.getGaia().getTemporaryPatch();
				try {
					temporaryPatch.copyFrom(patch);
					temporaryPatch.saveParameters();
					gaiaTool.getLog().log("Patch copied to temporary patch.");
				} catch (IncompletePatchException e) {
					if (patch instanceof GaiaPatch) {
						new PatchDataRequester((GaiaPatch) patch, new PatchCompleteListener() {
							public void patchComplete(GaiaPatch patch) {
								copyToTemporaryPatch(); // try again
							}
						}).requestMissingParameters();
					} else {
						gaiaTool.getLog().log(e.getMessage());
					}
				}
			}
		}
		
		public class ToneTreeNode extends ContentSelectionTreeNode {
			private static final long serialVersionUID = 1L;
			
			int tone;
			
			public ToneTreeNode(int tone) {
				this.tone = tone;
			}
			
			public String toString() {
				return "Tone " + tone;
			}
			
			@Override
			public JComponent getContentView() {
				if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
					return new NotConnectedPanel(gaiaTool.getGaia());
				
				return new ToneView(patch, tone);
			}
		}
		
		public class DistortionTreeNode extends ContentSelectionTreeNode {
			private static final long serialVersionUID = 1L;
			
			public String toString() {
				return "Distortion";
			}
			
			@Override
			public JComponent getContentView() {
				if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
					return new NotConnectedPanel(gaiaTool.getGaia());
				
//				return new DistortionPanel(patch);
				return new DistortionView(patch);
			}
		}
		
		public class FlangerTreeNode extends ContentSelectionTreeNode {
			private static final long serialVersionUID = 1L;
			
			public String toString() {
				return "Flanger";
			}
			
			@Override
			public JComponent getContentView() {
				if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
					return new NotConnectedPanel(gaiaTool.getGaia());
				
				return new FlangerView(patch);
			}
		}
		
		public class DelayTreeNode extends ContentSelectionTreeNode {
			private static final long serialVersionUID = 1L;
			
			public String toString() {
				return "Delay";
			}
			
			@Override
			public JComponent getContentView() {
				if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
					return new NotConnectedPanel(gaiaTool.getGaia());
				
				return new DelayView(patch);
			}
		}
		
		public class ReverbTreeNode extends ContentSelectionTreeNode {
			private static final long serialVersionUID = 1L;
			
			public String toString() {
				return "Reverb";
			}
			
			@Override
			public JComponent getContentView() {
				if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
					return new NotConnectedPanel(gaiaTool.getGaia());
				
				return new ReverbView(patch);
			}
		}
		
		public class ArpeggioTreeNode extends ContentSelectionTreeNode {
			private static final long serialVersionUID = 1L;
			
			public String toString() {
				return "Arpeggio";
			}
			
			@Override
			public JComponent getContentView() {
				if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
					return new NotConnectedPanel(gaiaTool.getGaia());
				
				return new ArpeggioView(patch);
			}
		}
		
	}
	
}
