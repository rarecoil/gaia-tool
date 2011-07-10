package nl.grauw.gaia_tool.views;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.GaiaTool;

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
	}
	
	private DefaultTreeModel createContentSelectionTreeModel() {
		ContentSelectionTreeNode rootNode = new ContentSelectionTreeNode("Parameters");
		SystemTreeNode systemNode = new SystemTreeNode();
		rootNode.add(systemNode);
		PatchTreeNode temporaryPatchNode = new PatchTreeNode("Temporary patch", gaiaTool.getGaia().getTemporaryPatch());
		rootNode.add(temporaryPatchNode);
		ContentSelectionTreeNode userPatchesNode = new ContentSelectionTreeNode("User patches");
		for (int bank = 0; bank < 8; bank++) {
			ContentSelectionTreeNode bankNode = new ContentSelectionTreeNode("Bank " + "ABCDEFGH".charAt(bank));
			for (int patch = 0; patch < 8; patch++) {
				String patchName = "Patch " + "ABCDEFGH".charAt(bank) + "-" + (patch + 1);
				PatchTreeNode patchNode = new PatchTreeNode(patchName, gaiaTool.getGaia().getUserPatch(bank, patch));
				bankNode.add(patchNode);
			}
			userPatchesNode.add(bankNode);
		}
		rootNode.add(userPatchesNode);
		return new DefaultTreeModel(rootNode);
	}
	
	/**
	 * Returns a panel corresponding to the current selection in the tree.
	 * @return A panel that is to be shown, or null if the current panel should
	 *         hold off updating for a little longer (to avoid flickering).
	 */
	public JPanel getSelectedContentView() {
		TreePath tp = getSelectionPath();
		if (tp == null) {
			return new IntroPanel();
		} else if (!gaiaTool.getGaia().isOpened() || !gaiaTool.getGaia().isIdentityConfirmed()) {
			return new NotConnectedPanel(gaiaTool.getGaia());
		} else {
			ContentSelectionTreeNode selectedNode = (ContentSelectionTreeNode)tp.getLastPathComponent();
			return selectedNode.getContentView();
		}
	}
	
	/**
	 * Return the patch that is currently selected in the tree.
	 * @return The currently selected patch, or null.
	 */
	public GaiaPatch getSelectedPatch() {
		TreePath tp = getSelectionPath();
		if (tp != null) {
			for (int i = tp.getPathCount() - 1; i >= 0; i--) {
				if (tp.getPathComponent(i) instanceof PatchTreeNode) {
					return ((PatchTreeNode)tp.getPathComponent(i)).getPatch();
				}
			}
		}
		return null;
	}
	
	public class SystemTreeNode extends ContentSelectionTreeNode {
		private static final long serialVersionUID = 1L;
		
		public String toString() {
			return "System";
		}
		
		@Override
		public JPanel getContentView() {
			return new SystemView(gaiaTool.getGaia());
		}
	}
	
}
