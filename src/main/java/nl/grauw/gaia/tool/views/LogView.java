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
package nl.grauw.gaia.tool.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.DefaultCaret;

import nl.grauw.gaia.Log;
import nl.grauw.gaia.Log.LogUpdateListener;

public class LogView extends JPanel {
	
	private Log log;

	private static final long serialVersionUID = 1L;
	private JTextArea logArea;
	private JScrollPane logScrollPane;
	
	public LogView(Log log) {
		this.log = log;
		
		initComponents();
		this.addAncestorListener(new LogUpdateProxy());
	}
	
	public void onLogUpdate(Log source) {
		if (source == log) {
			int logTextLength = log.getLog().length();
			int logAreaLength = logArea.getText().length();
			if (logTextLength > logAreaLength + 1) {	// skip the trailing newline
				logArea.append(log.getLog().substring(logAreaLength, logTextLength - 1));
			} else if (logTextLength < logAreaLength) {
				throw new RuntimeException("Length mismatch. Something is amiss here.");
			}
		}
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		add(getLogScrollPane());
	}

	private JScrollPane getLogScrollPane() {
		if (logScrollPane == null) {
			logScrollPane = new JScrollPane();
			logScrollPane.setViewportView(getLogArea());
		}
		return logScrollPane;
	}

	private JTextArea getLogArea() {
		if (logArea == null) {
			logArea = new JTextArea();
			logArea.setEditable(false);
			logArea.setLineWrap(true);
			logArea.setWrapStyleWord(true);
			DefaultCaret caret = (DefaultCaret) logArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);	// scroll along with text changes
		}
		return logArea;
	}

	public class LogUpdateProxy implements AncestorListener, LogUpdateListener {
		
		@Override
		public void onLogUpdate(final Log source) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					LogView.this.onLogUpdate(source);
				}
			});
		}
		
		@Override
		public void ancestorAdded(AncestorEvent event) {
			log.addUpdateListener(this);
			LogView.this.onLogUpdate(log);
		}
		
		@Override
		public void ancestorRemoved(AncestorEvent event) {
			log.removeUpdateListener(this);
		}
		
		@Override
		public void ancestorMoved(AncestorEvent event) {
		}
		
	}
	
}
