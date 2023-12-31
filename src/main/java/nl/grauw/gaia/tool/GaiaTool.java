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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFileChooser;

import nl.grauw.gaia.Gaia;
import nl.grauw.gaia.GaiaPatch;
import nl.grauw.gaia.Library;
import nl.grauw.gaia.Log;
import nl.grauw.gaia.Parameters;
import nl.grauw.gaia.Patch;
import nl.grauw.gaia.PatchDataRequester;
import nl.grauw.gaia.PatchLoader;
import nl.grauw.gaia.PatchSaver;
import nl.grauw.gaia.PatchDataRequester.PatchCompleteListener;
import nl.grauw.gaia.midi.JavaMidiConnection;
import nl.grauw.gaia.midi.JavaMidiConnection.GaiaNotFoundException;

public class GaiaTool {
	
	private JavaMidiConnection midiConnection;
	private Gaia gaia;
	private Log log;
	private Library library;
	
	private Properties settings = new Properties();
	private File currentDirectory = null;
	
	public final static boolean DEBUG = false;
	
	public GaiaTool() {
		log = new Log();
		midiConnection = new JavaMidiConnection(log, settings);
		gaia = new Gaia(log, midiConnection);
		library = new Library(getLibraryPath());
		
		loadSettings();
	}
	
	public JavaMidiConnection getMidiConnection() {
		return midiConnection;
	}
	
	public Log getLog() {
		return log;
	}
	
	public Gaia getGaia() {
		return gaia;
	}
	
	public Library getLibrary() {
		return library;
	}
	
	public void openGaia() throws MidiUnavailableException, GaiaNotFoundException {
		midiConnection.open();
		gaia.open();
	}
	
	public void closeGaia() {
		if (gaia.isOpened())
			gaia.close();
		if (midiConnection.isOpened())
			midiConnection.close();
	}
	
	public void exit() {
		closeGaia();
		saveSettings();
		java.lang.System.exit(0);
	}
	
	private void loadSettings() {
		try {
			FileReader fr = new FileReader(new File(getAndCreateSettingsPath(), "settings.properties"));
			settings.load(fr);
		} catch (FileNotFoundException e) {
			// no settings file yet
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveSettings() {
		try {
			FileWriter fw = new FileWriter(new File(getAndCreateSettingsPath(), "settings.properties"));
			settings.store(fw, "GAIA tool settings file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves a patch to the specified file.
	 * @param patchFile
	 * @param patch
	 */
	public void savePatch(final File patchFile, Patch patch) {
		log.log("Saving patch to " + patchFile + "…");
		// TODO: reload patch from GAIA and compare with local values before saving
		if (patch instanceof GaiaPatch) {
			new PatchDataRequester((GaiaPatch) patch, new PatchCompleteListener() {
				@Override
				public void onPatchComplete(GaiaPatch patch) {
					savePatchContinue(patchFile, patch);
				}
			}).requestMissingParameters();
		} else {
			savePatchContinue(patchFile, patch);
		}
	}
	
	private void savePatchContinue(File patchFile, Patch patch) {
		try {
			new PatchSaver(patch).save(patchFile);
		} catch (IOException e) {
			log.log("Saving failed: " + e.getMessage());
		}
		log.log("Saving complete.");
		library.refresh(patchFile);
	}
	
	/**
	 * Loads a patch from a file into the given patch object.
	 * @param patchFile
	 * @param patch
	 */
	public void loadPatch(File patchFile, Patch patch) {
		log.log("Loading patch from " + patchFile + "…");
		try {
			new PatchLoader(patch).load(patchFile);
		} catch (IOException e) {
			log.log("Loading failed: " + e.getMessage());
		}
		// XXX: No loading complete message if invoked directly.
	}
	
	/**
	 * Loads a patch from a file into a Gaia patch, and then sync it to the device.
	 * @param patchFile
	 * @param patch
	 */
	public void loadGaiaPatch(File patchFile, GaiaPatch patch) {
		loadPatch(patchFile, patch);
		for (Parameters p : patch) {
			gaia.sendDataTransmission(p);
		}
		log.log("Loading complete.");
	}
	
	/**
	 * Returns a File object for the settings path.
	 * Note: also creates the settings directory if it does not exist.
	 * @return The settings path.
	 */
	private File getAndCreateSettingsPath() {
		File path = getSettingsPath();
		if (!path.exists()) {
			path.mkdirs();
		}
		return path;
	}
	
	private File getSettingsPath() {
		String appData = java.lang.System.getenv("APPDATA");
		String home = java.lang.System.getProperty("user.home");
		if (appData != null) {
			return new File(appData, "gaia-tool");
		} else if (home != null) {
			return new File(home, ".gaia-tool");
		}
		throw new RuntimeException("Home directory not found.");
	}
	
	public File getLibraryPath() {
		File home = new JFileChooser().getFileSystemView().getDefaultDirectory();
		return new File(home, "GAIA/Library");
	}
	
	/**
	 * Returns the current directory for the file open/close dialogs.
	 * @return The directory to load/save in.
	 */
	public File getCurrentDirectory() {
		if (currentDirectory == null && getLibraryPath().exists())
			return getLibraryPath();
		return currentDirectory;
	}
	
	/**
	 * Sets the current directory for the file open/close dialogs.
	 * @param directory The directory to load/save in.
	 */
	public void setCurrentDirectory(File directory) {
		currentDirectory = directory;
	}
	
}
