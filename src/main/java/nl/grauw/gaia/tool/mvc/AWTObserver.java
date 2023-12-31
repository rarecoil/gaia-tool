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
package nl.grauw.gaia.tool.mvc;

/**
 * This interface is intended for observers that need to be notified on the AWT event thread.
 * 
 * AWT observers are also weakly referenced, allowing them to get garbage collected if they
 * are no longer referenced elsewhere, making un-registering unnecessary.
 */
public interface AWTObserver {
	
	/**
	 * Invoked when the state of an observed object changes.
	 * @param source The observed object.
	 * @param detail Object providing details on the state change.
	 */
	public void update(Observable source, Object detail);
	
}
