package nl.grauw.gaia_tool.views;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import nl.grauw.gaia_tool.GaiaPatch;

public class PatchTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
	
	String name;
	GaiaPatch patch;
	
	public PatchTreeNode(String name, GaiaPatch patch) {
		super(name);
		this.name = name;
		this.patch = patch;
		
		add(new DefaultMutableTreeNode("Tone 1"));
		add(new DefaultMutableTreeNode("Tone 2"));
		add(new DefaultMutableTreeNode("Tone 3"));
		add(new DefaultMutableTreeNode("Distortion"));
		add(new DefaultMutableTreeNode("Flanger"));
		add(new DefaultMutableTreeNode("Delay"));
		add(new DefaultMutableTreeNode("Reverb"));
		add(new DefaultMutableTreeNode("Arpeggio"));
	}
	
	public GaiaPatch getPatch() {
		return patch;
	}
	
	public String toString() {
		return name;
	}
	
	public JPanel getContentView(TreeNode node) {
		if (node == this) {
			return new PatchView(patch);
		} else if (node.getParent() != this) {
			throw new RuntimeException("Node must be a child or self.");
		} else {
			String desc = node.toString();
			if ("Tone 1".equals(desc)) {
				return new ToneView(patch, 1);
			} else if ("Tone 2".equals(desc)) {
				return new ToneView(patch, 2);
			} else if ("Tone 3".equals(desc)) {
				return new ToneView(patch, 3);
			} else if ("Distortion".equals(desc)) {
//				return new DistortionPanel(patch);
				return new DistortionView(patch);
			} else if ("Flanger".equals(desc)) {
				return new FlangerView(patch);
			} else if ("Delay".equals(desc)) {
				return new DelayView(patch);
			} else if ("Reverb".equals(desc)) {
				return new ReverbView(patch);
			} else if ("Arpeggio".equals(desc)) {
				return new ArpeggioView(patch);
			}
		}
		throw new RuntimeException("Parameters not found.");
	}
	
}
