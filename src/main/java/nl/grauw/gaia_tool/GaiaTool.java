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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class GaiaTool {
	
	private Gaia gaia;
	private Log log;
	
	private Properties settings = new Properties();
	private File currentDirectory = null;
	
	public GaiaTool() {
		log = new Log();
		gaia = new Gaia(log, settings);
		
		loadSettings();
	}
	
	public Log getLog() {
		return log;
	}
	
	public Gaia getGaia() {
		return gaia;
	}
	
	public void exit() {
		gaia.close();
		saveSettings();
		java.lang.System.exit(0);
	}
	
	private void loadSettings() {
		FileReader fr;
		try {
			fr = new FileReader(new File(getAndCreateSettingsPath(), "settings.properties"));
			settings.load(fr);
		} catch (IllegalArgumentException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveSettings() {
		FileWriter fw;
		try {
			fw = new FileWriter(new File(getAndCreateSettingsPath(), "settings.properties"));
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
	public void savePatch(File patchFile, Patch patch) {
		log.log("Saving patch to " + patchFile + "…");
		new PatchSaver(patch).save(patchFile);
		// TODO: indicate when the file has finished saving.
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
		// TODO: indicate when the file has finished loading.
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
		// TODO: indicate when the file has finished loading.
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
	
	/**
	 * Returns the current directory for the file open/close dialogs.
	 * @return The directory to load/save in.
	 */
	public File getCurrentDirectory() {
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
