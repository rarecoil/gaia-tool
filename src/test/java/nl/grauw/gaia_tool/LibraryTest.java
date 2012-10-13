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

import java.io.File;

import org.junit.Test;

public class LibraryTest {

	@Test
	public void testGetName() {
		Library library = new Library(new File("/test/xxx"));
		assertEquals("xxx", library.getName());
	}

	@Test
	public void testPopulateNonExistantPath() {
		Library library = new Library(new File("non-existant-test-path"));
		library.refresh();
		assertEquals(true, library.getPatches().isEmpty());
		assertEquals(true, library.getLibraries().isEmpty());
	}

}
