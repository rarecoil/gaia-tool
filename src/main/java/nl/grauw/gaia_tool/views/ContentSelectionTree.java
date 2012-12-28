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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nl.grauw.gaia_tool.FilePatch;
import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.GaiaTool;
import nl.grauw.gaia_tool.Library;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.SVDPatch;
import nl.grauw.gaia_tool.SVDPatchGroup;
import nl.grauw.gaia_tool.Patch.IncompletePatchException;
import nl.grauw.gaia_tool.PatchDataRequester;
import nl.grauw.gaia_tool.PatchDataRequester.PatchCompleteListener;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.UserPatch;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;

public class ContentSelectionTree extends JTree {
	private static final long serialVersionUID = 1L;
	
	private GaiaTool gaiaTool;
	private DefaultTreeModel treeModel;
	
	public ContentSelectionTree(GaiaTool gaiaTool) {
		this.gaiaTool = gaiaTool;
		
		treeModel = createContentSelectionTreeModel();
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
		PatchTreeNode temporaryPatchNode = new PatchTreeNode(gaiaTool.getGaia().getTemporaryPatch());
		rootNode.add(temporaryPatchNode);
		ContentSelectionTreeNode userPatchesNode = new UserPatchesNode(gaiaTool.getGaia());
		rootNode.add(userPatchesNode);
		LibraryNode libraryNode = new LibraryNode(gaiaTool.getLibrary(), "Library");
		addTreeWillExpandListener(libraryNode);
		rootNode.add(libraryNode);
		return new DefaultTreeModel(rootNode);
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
	
	public class UserPatchesNode extends ContentSelectionTreeNode {
		private static final long serialVersionUID = 1L;
		
		public UserPatchesNode(Gaia gaia) {
			super("User patches");
			
			for (int bank = 0; bank < 8; bank++) {
				ContentSelectionTreeNode bankNode = new ContentSelectionTreeNode("Bank " + "ABCDEFGH".charAt(bank));
				for (int patch = 0; patch < 8; patch++) {
					PatchTreeNode patchNode = new PatchTreeNode(gaiaTool.getGaia().getUserPatch(bank, patch));
					bankNode.add(patchNode);
				}
				add(bankNode);
			}
		}
	}
	
	public class LibraryNode extends ContentSelectionTreeNode implements AWTObserver, TreeWillExpandListener {
		private static final long serialVersionUID = 1L;

		private Library library;
		private JPopupMenu contextMenu;
		
		public LibraryNode(Library library) {
			this(library, library.getName());
		}
		
		public LibraryNode(Library library, String name) {
			super(name);
			this.library = library;
			library.addObserver(this);
			
			populateChildren();
		}
		
		private void populateChildren() {
			for (Library sublibrary : library.getLibraries()) {
				add(new LibraryNode(sublibrary));
			}
			for (SVDPatchGroup svdPatchGroup : library.getSVDPatchGroups()) {
				add(new SVDPatchGroupNode(svdPatchGroup));
			}
			for (Patch patch : library.getPatches()) {
				add(new PatchTreeNode(patch));
			}
		}
		
		@Override
		public void update(Observable source, Object detail) {
			removeAllChildren();
			populateChildren();
			treeModel.nodeStructureChanged(this);
		}
		
		@Override
		public boolean isLeaf() {
			return false;
		}
		
		@Override
		public JComponent getContentView() {
			return new LibraryPanel(gaiaTool);
		}
		
		public JPopupMenu getContextMenu() {
			if (contextMenu == null) {
				contextMenu = new LibraryContextMenu();
			}
			return contextMenu;
		}
		
		@Override
		public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
			if (library.isEmpty())
				library.refresh();
		}
		
		@Override
		public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		}
		
		private class LibraryContextMenu extends JPopupMenu implements ActionListener {
			private static final long serialVersionUID = 1L;
			
			private JMenuItem refresh;
			
			public LibraryContextMenu() {
				refresh = new JMenuItem("Refresh");
				refresh.addActionListener(this);
				add(refresh);
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == refresh) {
					refresh();
				}
			}
			
			private void refresh() {
				library.refresh();
			}
		}
		
	}
	
	public class SVDPatchGroupNode extends ContentSelectionTreeNode {
		private static final long serialVersionUID = 1L;
		
		public SVDPatchGroupNode(SVDPatchGroup svdPatches) {
			super(svdPatches.getName());
			
			for (int bank = 0; bank < 8; bank++) {
				ContentSelectionTreeNode bankNode = new ContentSelectionTreeNode("Bank " + "ABCDEFGH".charAt(bank));
				for (int patch = 0; patch < 8; patch++) {
					PatchTreeNode patchNode = new PatchTreeNode(svdPatches.getPatch(bank, patch));
					bankNode.add(patchNode);
				}
				add(bankNode);
			}
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
		
		private Patch patch;
		private JPopupMenu contextMenu;
		
		public PatchTreeNode(Patch patch) {
			this.patch = patch;
		}
		
		public Patch getPatch() {
			return patch;
		}
		
		public String toString() {
			if (patch instanceof TemporaryPatch)
				return "Temporary patch";
			if (patch instanceof UserPatch)
				return "Patch " + "ABCDEFGH".charAt(((UserPatch)patch).getBank()) + "-" + (((UserPatch)patch).getPatch() + 1);
			if (patch instanceof SVDPatch)
				return "Patch " + "ABCDEFGH".charAt(((SVDPatch)patch).getBank()) + "-" + (((SVDPatch)patch).getPatch() + 1);
			if (patch instanceof FilePatch)
				return ((FilePatch)patch).getName();
			return "Unknown patch";
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
		
		private class PatchContextMenu extends JPopupMenu implements ActionListener {
			private static final long serialVersionUID = 1L;
			
			private JMenuItem copy;
			private JMenuItem refresh;
			
			public PatchContextMenu() {
				copy = new JMenuItem("Copy to temporary patch");
				copy.addActionListener(this);
				add(copy);
				
				// Add refresh option to file patches
				if (patch instanceof GaiaPatch || patch instanceof FilePatch || patch instanceof SVDPatch) {
					refresh = new JMenuItem("Refresh");
					refresh.addActionListener(this);
					add(refresh);
				}
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
				if (e.getSource() == refresh) {
					refresh();
				}
			}
			
			public void copyToTemporaryPatch() {
				if (patch instanceof TemporaryPatch)
					throw new RuntimeException("Can not copy from temporary patch.");
				if (!gaiaTool.getGaia().isConnected())
					throw new RuntimeException("Gaia not connected.");
				
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
			
			private void refresh() {
				try {
					if (patch instanceof GaiaPatch)
						((GaiaPatch)patch).load();
					else if (patch instanceof FilePatch)
						((FilePatch)patch).load();
					else if (patch instanceof SVDPatch)
						((SVDPatch)patch).load();
					else
						throw new RuntimeException("Patch type can not be refreshed.");
				} catch (FileNotFoundException e) {
					gaiaTool.getLog().log(e.getMessage());
					if (PatchTreeNode.this.getParent() instanceof LibraryNode)
						((LibraryNode)PatchTreeNode.this.getParent()).library.refresh();
				} catch (IOException e) {
					gaiaTool.getLog().log(e.getMessage());
				}
			}
			
		}
		
	}
	
}
