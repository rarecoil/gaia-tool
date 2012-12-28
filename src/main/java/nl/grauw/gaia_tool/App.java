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
package nl.grauw.gaia_tool;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import nl.grauw.gaia_tool.Gaia.GaiaNotFoundException;
import nl.grauw.gaia_tool.views.GaiaToolView;

public class App {
	
	static App app;
	
	GaiaTool gaiaTool;
	GaiaToolView gaiaToolView;
	
	public static void main(String[] args) {
		installLookAndFeel();
		app = new App();
		app.initialiseExceptionHandler();
		app.initialiseModel();
		app.initialiseView();
	}
	
	public static void installLookAndFeel() {
		try {
			String preferredPLAF = "Mac OS X".equals(System.getProperty("os.name")) ? "Aqua" : "Nimbus";
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if (preferredPLAF.equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		// also make tooltips stick around longer and behave nicer
		ToolTipManager ttm = ToolTipManager.sharedInstance();
		ttm.setDismissDelay(30000);
		ttm.setReshowDelay(0);
	}
	
	public void initialiseModel() {
		gaiaTool = new GaiaTool();
		try {
			gaiaTool.getGaia().open();
		} catch (GaiaNotFoundException e) {
			// that’s fine
		} catch(MidiUnavailableException e) {
			JOptionPane.showMessageDialog(null, e, "Error connecting to Roland GAIA SH-01.", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	public void initialiseView() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gaiaToolView = new GaiaToolView(gaiaTool);
				gaiaToolView.setVisible(true);
			}
		});
	}
	
	public void initialiseExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
	}
	
	private class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
		
		@Override
		public void uncaughtException(Thread thread, Throwable exception) {
			showErrorDialog(exception);
			System.exit(1);
		}
		
		private void showErrorDialog(Throwable exception) {
			String[] options = new String[] { "Exit" };
			JOptionPane.showOptionDialog(
				gaiaToolView,
				"[" + exception.getClass().getName() + "]\n\n" + exception.getMessage(),
				"An error has occurred.",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.ERROR_MESSAGE,
				null,
				options,
				options[0]
			);
		}
		
	}
	
}
