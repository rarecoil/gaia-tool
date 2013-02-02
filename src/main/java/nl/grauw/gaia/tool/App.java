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
package nl.grauw.gaia.tool;

import java.awt.Toolkit;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import nl.grauw.gaia.midi.JavaMidiConnection.GaiaNotFoundException;
import nl.grauw.gaia.tool.views.GaiaToolView;

public class App {
	
	static App app;
	
	GaiaTool gaiaTool;
	GaiaToolView gaiaToolView;
	
	public static void main(String[] args) {
		fixLinuxDoubleClick();
		installLookAndFeel();
		app = new App();
		app.initialiseExceptionHandler();
		app.initialiseModel();
		app.initialiseView();
	}
	
	/**
	 * Under Linux, Java 6 does not honour the multi/double-click speed preferences,
	 * and defaults to a 200 ms double-click, which is very fast and difficult to hit.
	 * (The default was changed to 500 ms in Java 7.)
	 * 
	 * So if this is the case, cheat and set the sun.awt.X11.XToolkit#awt_multiclick_time
	 * field to a higher value through reflection.
	 * 
	 * See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5076635
	 * See sun.awt.X11.XToolkit#getMultiClickTime()
	 */
	public static void fixLinuxDoubleClick() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		if ((Integer)(toolkit.getDesktopProperty("awt.multiClickInterval")) == 200) {
			try {
				Field field = toolkit.getClass().getDeclaredField("awt_multiclick_time");
				field.setAccessible(true);
				field.setInt(toolkit, 500);  // ms
			} catch (Exception e) {
			}
		}
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
			gaiaTool.openGaia();
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
			exception.printStackTrace();
			
			submitErrorLog(exception);
			showErrorDialog(exception);
			System.exit(1);
		}
		
		/**
		 * Submits exception stack trace to Loggly on a new thread.
		 * Optimistically hoping it can complete before the dialog is closed.
		 */
		private void submitErrorLog(final Throwable exception) {
			new Thread(new Runnable() {
				public void run() {
					try {
						System.out.println("Submitting exception stack trace...");
						
						URL url = new URL("https://logs.loggly.com/inputs/b2f0a92a-0ba8-4085-82f6-e217d20e5b6a");
						HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
						connection.setRequestMethod("POST");
						connection.setRequestProperty("Content-Type", "text/plain");
						connection.setDoOutput(true);
						
						PrintWriter writer = new PrintWriter(connection.getOutputStream());
						exception.printStackTrace(writer);
						writer.flush();
						writer.close();
						
						System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage());
						connection.disconnect();
					} catch (Exception e) {
					}
				}
			}).start();
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
