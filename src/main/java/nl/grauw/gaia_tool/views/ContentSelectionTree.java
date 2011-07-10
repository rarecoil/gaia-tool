package nl.grauw.gaia_tool.views;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.GaiaTool;
import nl.grauw.gaia_tool.Patch;

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
		if (tp == null)
			return new IntroPanel();
		
		ContentSelectionTreeNode selectedNode = (ContentSelectionTreeNode)tp.getLastPathComponent();
		return selectedNode.getContentView();
	}
	
	/**
	 * Return the patch that is currently selected in the tree.
	 * @return The currently selected patch, or null.
	 */
	public Patch getSelectedPatch() {
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
		public JPanel getContentView() {
			return new JPanel();
		}
		
	}
	
	public class SystemTreeNode extends ContentSelectionTreeNode {
		private static final long serialVersionUID = 1L;
		
		public String toString() {
			return "System";
		}
		
		@Override
		public JPanel getContentView() {
			if (!gaiaTool.getGaia().isConnected())
				return new NotConnectedPanel(gaiaTool.getGaia());
			
			return new SystemView(gaiaTool.getGaia());
		}
	}
	
	public class PatchTreeNode extends ContentSelectionTreeNode {
		private static final long serialVersionUID = 1L;
		
		String name;
		Patch patch;
		
		public PatchTreeNode(String name, Patch patch) {
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
		public JPanel getContentView() {
			if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
				return new NotConnectedPanel(gaiaTool.getGaia());
			
			return new PatchView(patch);
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
			public JPanel getContentView() {
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
			public JPanel getContentView() {
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
			public JPanel getContentView() {
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
			public JPanel getContentView() {
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
			public JPanel getContentView() {
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
			public JPanel getContentView() {
				if (patch instanceof GaiaPatch && !gaiaTool.getGaia().isConnected())
					return new NotConnectedPanel(gaiaTool.getGaia());
				
				return new ArpeggioView(patch);
			}
		}
		
	}
	
}
