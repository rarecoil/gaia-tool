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

public class IntroPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel title;
	private JLabel copyright;
	private JLabel introduction;
	private JLabel instructions;
	private JLabel notice;
	
	public IntroPanel() {
		initComponents();
	}

	private void initComponents() {
		title = new JLabel("<html>Welcome to the Roland GAIA SH-01 tool</html>");
		copyright = new JLabel("<html>Copyright © 2010 Laurens Holst</html>");
		introduction = new JLabel("<html>With this tool you can view your Roland GAIA’s settings, " +
				"such as patch names, patch parameters, arpeggio patterns and system settings.</html>");
		instructions = new JLabel("<html>Use the tree on the left to select the system or patch parameters " +
				"that you want to view.</html>");
		notice = new JLabel("<html>This is free software licensed under the Apache 2.0 license. " +
				"For more information and downloads visit the GAIA tool project page at " +
				"http://www.grauw.nl/projects/gaia-tool/.</html>");
		
		title.setFont(title.getFont().deriveFont(24f).deriveFont(Font.BOLD));
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(title)
					.addComponent(copyright)
					.addComponent(introduction)
					.addComponent(instructions)
					.addComponent(notice)
			);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(title)
					.addComponent(copyright)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(introduction)
					.addComponent(instructions)
					.addComponent(notice)
			);
	}

}
