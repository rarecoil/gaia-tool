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
		assertEquals(false, library.getPatches().iterator().hasNext());
		assertEquals(false, library.getLibraries().iterator().hasNext());
	}

}
