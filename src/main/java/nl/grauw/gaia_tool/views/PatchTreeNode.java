package nl.grauw.gaia_tool.views;

import javax.swing.tree.DefaultMutableTreeNode;

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
	
}
