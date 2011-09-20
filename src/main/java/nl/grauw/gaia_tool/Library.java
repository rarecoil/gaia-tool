package nl.grauw.gaia_tool;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import nl.grauw.gaia_tool.mvc.Observable;

public class Library extends Observable {
	
	private List<FilePatch> patches = new Vector<FilePatch>();
	private List<Library> libraries = new Vector<Library>();
	private File source;
	
	public Library(File source) {
		this.source = source;
	}
	
	public Iterable<FilePatch> getPatches() {
		return patches;
	}
	
	public Iterable<Library> getLibraries() {
		return libraries;
	}
	
	public File getSource() {
		return source;
	}
	
	public String getName() {
		return source.getName();
	}
	
	/**
	 * Populate the library contents.
	 */
	public void populate() {
		populatePatches();
		populateLibraries();
		
		notifyObservers();
	}
	
	/**
	 * Refresh a patch on a specific path, or the library containing it.
	 * @param path The path to the changed file.
	 */
	public void refresh(File path) {
		if (path.getPath().startsWith(source.getPath() + File.separator)) {
			if (path.getParentFile().equals(source)) {
				boolean patchMatched = false;
				for (FilePatch patch : patches) {
					if (patch.getSource().equals(path)) {
						try {
							patch.load();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				if (!patchMatched)
					populate();
			} else {
				for (Library library : libraries) {
					library.refresh(path);
				}
			}
		}
	}
	
	/**
	 * Populate .gaia-files as patches.
	 */
	private void populatePatches() {
		patches.clear();
		
		File[] files = source.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".gaia");
			}
		});
		
		if (files == null)
			return;
		
		for (File patchFile : files) {
			FilePatch patch = new FilePatch(patchFile);
			try {
				patch.load();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			patches.add(patch);
		}
	}
	
	/**
	 * Populate subdirectories as sub-libraries.
	 */
	private void populateLibraries() {
		libraries.clear();
		
		File[] files = source.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			}
		});
		
		if (files == null)
			return;
		
		for (File libraryDirectory : files) {
			Library library = new Library(libraryDirectory);
			library.populate();
			if (library.patches.size() > 0 || library.libraries.size() > 0) {
				libraries.add(library);
			}
		}
	}
	
}
