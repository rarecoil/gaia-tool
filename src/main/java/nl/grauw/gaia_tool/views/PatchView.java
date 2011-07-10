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

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import nl.grauw.gaia_tool.Gaia;
import nl.grauw.gaia_tool.Parameters;
import nl.grauw.gaia_tool.GaiaPatch;
import nl.grauw.gaia_tool.Patch;
import nl.grauw.gaia_tool.TemporaryPatch;
import nl.grauw.gaia_tool.UserPatch;
import nl.grauw.gaia_tool.mvc.AWTObserver;
import nl.grauw.gaia_tool.mvc.Observable;
import nl.grauw.gaia_tool.views.parameters.PatchCommonView;

public class PatchView extends ParametersView implements AWTObserver {

	private static final long serialVersionUID = 1L;
	
	JPanel parametersContainer;
	PatchCommonView patchCommonView;
	
	private Patch patch;
	
	public PatchView(Patch patch) {
		this.patch = patch;
		patch.addObserver(this);
		if (patch.getCommon() == null)
			loadParameters();
		initComponents();
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
		}
		return "Patch";
	}
	
	@Override
	protected boolean isSyncShown() {
		return patch instanceof TemporaryPatch;
	}

	@Override
	protected JPanel getParametersContainer() {
		if (parametersContainer == null) {
			parametersContainer = new JPanel();
			parametersContainer.setLayout(new BoxLayout(parametersContainer, BoxLayout.Y_AXIS));
			PatchCommonView pcv = getPatchCommonView();
			if (pcv != null)
				parametersContainer.add(pcv);
		}
		return parametersContainer;
	}
	
	private PatchCommonView getPatchCommonView() {
		if (patchCommonView == null || patchCommonView.getParameters() != patch.getCommon()) {
			if (getParameters() != null) {
				patchCommonView = new PatchCommonView(patch.getCommon());
			}
		}
		return patchCommonView;
	}
	
	@Override
	public void update(Observable source, Object detail) {
		if ("common".equals(detail)) {
			parametersContainer.removeAll();
			PatchCommonView pcv = getPatchCommonView();
			if (pcv != null)
				parametersContainer.add(pcv);
			parametersContainer.revalidate();
			if (patch.getCommon() == null && isShowing()) {
				loadParameters();
			}
		}
	}

}
