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

import javax.swing.JPanel;

import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.parameters.PatchCommon;

public class PatchCommonView extends JPanel implements AWTObserver{
	private static final long serialVersionUID = 1L;
	
	private Patch patch;
	
	private nl.grauw.gaia_tool.views.parameters.PatchCommonView patchCommonView;
	
	public PatchCommonView(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		initComponents();
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		
		nl.grauw.gaia_tool.views.parameters.PatchCommonView pcv = getPatchCommonView();
		if (pcv != null)
			add(pcv);
	}
	
	@Override
	public void update(Observable source, Object detail) {
		if ("common".equals(detail) && source == patch) {
			nl.grauw.gaia_tool.views.parameters.PatchCommonView pcv = getPatchCommonView();
			if (getComponentCount() == 0 || getComponent(0) != pcv) {
				removeAll();
				if (pcv != null)
					add(pcv);
				revalidate();
			}
		}
	}
	
	public nl.grauw.gaia_tool.views.parameters.PatchCommonView getPatchCommonView() {
		PatchCommon pc = patch.getCommon();
		if (patchCommonView == null || patchCommonView.getParameters() != pc) {
			if (pc != null)
				patchCommonView = new nl.grauw.gaia_tool.views.parameters.PatchCommonView(pc);
		}
		return patchCommonView;
	}
	
}
