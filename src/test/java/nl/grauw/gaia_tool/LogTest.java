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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Grauw
 *
 */
public class LogTest {

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Log#getLog()}.
	 */
	@Test
	public void testGetLog() {
		Log l = new Log();
		assertTrue("".equals(l.getLog()));
	}

	/**
	 * Test method for {@link nl.grauw.gaia_tool.Log#log(Object)}.
	 */
	@Test
	public void testLog() {
		Log l = new Log();
		l.log("test1");
		l.log("test2");
		l.log(3);
		assertTrue("test1\ntest2\n3\n".equals(l.getLog()));
	}

}
