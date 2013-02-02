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
package nl.grauw.gaia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Log {
	
	private StringBuffer log;
	
	private List<LogUpdateListener> updateListeners = Collections.synchronizedList(new ArrayList<LogUpdateListener>());
	
	public Log() {
		log = new StringBuffer();
	}
	
	public void log(Object message) {
		log.append(message.toString());
		log.append("\n");
		
		fireLogUpdate();
	}
	
	public String getLog() {
		return log.toString();
	}
	
	public void addUpdateListener(LogUpdateListener listener) {
		updateListeners.add(listener);
	}
	
	public void removeUpdateListener(LogUpdateListener listener) {
		updateListeners.remove(listener);
	}
	
	private void fireLogUpdate() {
		for (LogUpdateListener listener : new ArrayList<LogUpdateListener>(updateListeners))
			listener.onLogUpdate(this);
	}
	
	public interface LogUpdateListener {
		public void onLogUpdate(Log source);
	}
	
}
