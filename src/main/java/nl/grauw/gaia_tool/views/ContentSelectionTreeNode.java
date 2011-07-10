package nl.grauw.gaia_tool.views;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

public class ContentSelectionTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
	
	public ContentSelectionTreeNode() {
		super();
	}
	
	public ContentSelectionTreeNode(String label) {
		super(label);
	}
	
	public JPanel getContentView() {
		return new JPanel();
	}
	
}
