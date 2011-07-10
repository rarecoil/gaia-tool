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

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import nl.grauw.gaia_tool.GaiaTool;

public class LibraryPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	GaiaTool gaiaTool;
	
	private JLabel title;
	private JLabel introduction;
	private JLabel introduction2;
	
	public LibraryPanel(GaiaTool gaiaTool) {
		this.gaiaTool = gaiaTool;
		
		initComponents();
	}

	private void initComponents() {
		title = new JLabel("<html>Patch library</html>");
		introduction = new JLabel("<html>For patches to show up in the library, store your patches " +
				"in the " + gaiaTool.getLibraryPath() + " folder.</html>");
		introduction2 = new JLabel("<html>Use the context menu to copy patches to the GAIA.</html>");
		
		title.setFont(title.getFont().deriveFont(24f).deriveFont(Font.BOLD));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(title)
					.addComponent(introduction)
					.addComponent(introduction2)
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(title)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(introduction)
					.addComponent(introduction2)
			);
	}

}
