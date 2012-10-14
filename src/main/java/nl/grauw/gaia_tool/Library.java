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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.grauw.gaia_tool.mvc.Observable;

public class Library extends Observable {
	
	private List<FilePatch> patches = new ArrayList<FilePatch>();
	private List<SVDPatchGroup> svdPatchGroups = new ArrayList<SVDPatchGroup>();
	private List<Library> libraries = new ArrayList<Library>();
	private File source;
	
	public Library(File source) {
		this.source = source;
	}
	
	public List<FilePatch> getPatches() {
		return Collections.unmodifiableList(patches);
	}

	public List<SVDPatchGroup> getSVDPatchGroups() {
		return Collections.unmodifiableList(svdPatchGroups);
	}
	
	public List<Library> getLibraries() {
		return Collections.unmodifiableList(libraries);
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
	public void refresh() {
		refreshPatches();
		refreshSVDPatchGroups();
		refreshLibraries();
		
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
					refresh();
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
	private void refreshPatches() {
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
	 * Populate .SVD-files as patch groups.
	 */
	private void refreshSVDPatchGroups() {
		svdPatchGroups.clear();
		
		File[] files = source.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".SVD");
			}
		});
		
		if (files == null)
			return;
		
		for (File svdPatchGroupFile : files) {
			SVDPatchGroup svdPatchGroup = new SVDPatchGroup(svdPatchGroupFile);
			try {
				svdPatchGroup.load();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			svdPatchGroups.add(svdPatchGroup);
		}
	}
	
	/**
	 * Populate subdirectories as sub-libraries.
	 */
	private void refreshLibraries() {
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
			library.refresh();
			if (!library.isEmpty()) {
				libraries.add(library);
			}
		}
	}
	
	private boolean isEmpty() {
		return patches.size() == 0 && svdPatchGroups.size() == 0 && libraries.size() == 0;
	}
	
}
