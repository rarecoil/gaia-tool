package nl.grauw.gaia_tool;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import nl.grauw.gaia_tool.mvc.Observable;

public class Library extends Observable {
	
	List<FilePatch> patches = new Vector<FilePatch>();
	
	private File source;
	
	public Library(File source) {
		this.source = source;
	}
	
	public void populate() {
		File[] files = source.listFiles(new GaiaFileFilter());
		if (files != null) {
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
	}
	
	private static class GaiaFileFilter implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".gaia");
		}
	}
	
}
