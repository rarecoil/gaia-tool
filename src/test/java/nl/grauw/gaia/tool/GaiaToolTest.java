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

import static org.junit.Assert.*;

import nl.grauw.gaia.Gaia;
import nl.grauw.gaia.Log;
import nl.grauw.gaia.tool.GaiaTool;

import org.junit.Test;

public class GaiaToolTest {

	@Test
	public void testGaiaTool() {
		new GaiaTool();
	}

	@Test
	public void testGetLog() {
		GaiaTool tool = new GaiaTool();
		assertTrue(tool.getLog() instanceof Log);
	}

	@Test
	public void testGetGaia() {
		GaiaTool tool = new GaiaTool();
		assertTrue(tool.getGaia() instanceof Gaia);
	}

}
