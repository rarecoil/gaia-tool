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
