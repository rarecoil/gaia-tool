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

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.grauw.gaia_tool.FilePatch;
import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.UserPatch;

public class PatchView extends ParametersView implements ChangeListener {

	private static final long serialVersionUID = 1L;
	
	JTabbedPane parametersContainer;
	PatchCommonView patchCommonView;
	
	private Patch patch;
	
	public PatchView(Patch patch) {
		this.patch = patch;
		initComponents();
		
		if (patch instanceof GaiaPatch && !patch.isComplete()) {
			((GaiaPatch)patch).load();
		}
	}
	
	public Parameters getParameters() {
		return patch.getCommon();
	}
	
	@Override
	public Gaia getGaia() {
		if (patch instanceof GaiaPatch)
			return ((GaiaPatch)patch).getGaia();
		return null;
	}
	
	@Override
	public void loadParameters() {
		if (patch instanceof GaiaPatch) {
			((GaiaPatch)patch).loadCommon();
		}
	}
	
	@Override
	public void saveParameters() {
		if (patch instanceof GaiaPatch) {
			((GaiaPatch)patch).saveModifiedParameters();
		}
	}
	
	@Override
	public String getTitle() {
		if (patch instanceof TemporaryPatch) {
			return "Temporary patch";
		} else if (patch instanceof UserPatch) {
			return "User patch " + "ABCDEFGH".charAt(((UserPatch)patch).getBank()) +
					"-" + (((UserPatch)patch).getPatch() + 1);
		} else if (patch instanceof FilePatch) {
			return "Patch " + ((FilePatch)patch).getName();
		}
		return "Patch";
	}
	
	@Override
	protected boolean isSyncShown() {
		return patch instanceof TemporaryPatch;
	}
	
	@Override
	protected JComponent getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JTabbedPane();
			parametersContainer.addChangeListener(this);
			
			parametersContainer.addTab("Common", new CommonTab());
			parametersContainer.addTab("Tone 1", new ToneTab(1));
			parametersContainer.addTab("Tone 2", new ToneTab(2));
			parametersContainer.addTab("Tone 3", new ToneTab(3));
			parametersContainer.addTab("Distortion", new DistortionTab());
			parametersContainer.addTab("Flanger", new FlangerTab());
			parametersContainer.addTab("Delay", new DelayTab());
			parametersContainer.addTab("Reverb", new ReverbTab());
			parametersContainer.addTab("Arpeggio", new ArpeggioTab());
			
			// add mnemonic keys ALT 1, ALT 2 ... ALT 0.
			for (int tabNr = 0; tabNr < 10 && tabNr < parametersContainer.getTabCount(); tabNr++) {
				parametersContainer.setMnemonicAt(tabNr, KeyEvent.VK_0 + ((tabNr + 1) % 10));
				parametersContainer.setDisplayedMnemonicIndexAt(tabNr, -1);
			}
		}
		return parametersContainer;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		((Tab)parametersContainer.getSelectedComponent()).tabSelected();
	}
	
	private abstract class Tab extends JPanel {
		private static final long serialVersionUID = 1L;
		
		abstract protected JComponent createContent();
		
		public Tab() {
			setLayout(new BorderLayout());
		}
		
		protected void tabSelected() {
			if (getComponentCount() == 0)
				add(createContent());
		}
	}
	
	private class CommonTab extends Tab {
		private static final long serialVersionUID = 1L;
		
		@Override
		public JPanel createContent() {
			return new PatchCommonView(patch);
		}
	}
	
	private class ToneTab extends Tab {
		private static final long serialVersionUID = 1L;
		
		private int toneNumber;
		
		public ToneTab(int toneNumber) {
			this.toneNumber = toneNumber;
		}
		
		@Override
		public JPanel createContent() {
			return new ToneView(patch, toneNumber);
		}
	}
	
	private class DistortionTab extends Tab {
		private static final long serialVersionUID = 1L;
		
		@Override
		public JPanel createContent() {
//			return new DistortionPanel(patch);
			return new DistortionView(patch);
		}
	}
	
	private class FlangerTab extends Tab {
		private static final long serialVersionUID = 1L;
		
		@Override
		public JPanel createContent() {
			return new FlangerView(patch);
		}
	}
	
	private class DelayTab extends Tab {
		private static final long serialVersionUID = 1L;
		
		@Override
		public JPanel createContent() {
			return new DelayView(patch);
		}
	}
	
	private class ReverbTab extends Tab {
		private static final long serialVersionUID = 1L;
		
		@Override
		public JPanel createContent() {
			return new ReverbView(patch);
		}
	}
	
	private class ArpeggioTab extends Tab {
		private static final long serialVersionUID = 1L;
		
		@Override
		public JPanel createContent() {
			return new ArpeggioView(patch);
		}
	}
	
}
