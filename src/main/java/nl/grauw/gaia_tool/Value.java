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

import nl.grauw.gaia_tool.Parameters.ParameterChange;
import nl.grauw.gaia_tool.mvc.Observable;

public class Value extends Observable {
	
	protected Parameters parameters;
	protected int offset;
	
	public Value(Parameters parameters, int offset) {
		this.parameters = parameters;
		this.offset = offset;
	}
	
	public Parameters getParameters() {
		return parameters;
	}
	
	/**
	 * Determine whether the value has changed based on information received from
	 * a Parameter observable update notification.
	 * 
	 * @param source The source of the update.
	 * @param pc The update’s event object.
	 * @return True if the value has changed.
	 */
	public boolean testChanged(Parameters source, ParameterChange pc) {
		return source == parameters && pc.includes(offset);
	}
	
}
