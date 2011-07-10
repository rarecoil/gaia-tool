package nl.grauw.gaia_tool.views;

import javax.swing.JPanel;

import nl.grauw.gaia_tool.GaiaPatch;

public class PatchTreeNode extends ContentSelectionTreeNode {
	private static final long serialVersionUID = 1L;
	
	String name;
	GaiaPatch patch;
	
	public PatchTreeNode(String name, GaiaPatch patch) {
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
	
	public GaiaPatch getPatch() {
		return patch;
	}
	
	public String toString() {
		return name;
	}
	
	@Override
	public JPanel getContentView() {
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
//			return new DistortionPanel(patch);
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
			return new ArpeggioView(patch);
		}
	}
	
}
