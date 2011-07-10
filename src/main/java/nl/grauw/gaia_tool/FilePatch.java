package nl.grauw.gaia_tool;

import java.io.File;
import java.io.IOException;

public class FilePatch extends Patch {
	
	private File source;
	
	public FilePatch(File source) {
		this.source = source;
	}
	
	public File getSource() {
		return source;
	}
	
	public String getName() {
		return source.getName();
	}
	
	public void load() throws IOException {
		new PatchLoader(this).load(source);
	}
	
}
