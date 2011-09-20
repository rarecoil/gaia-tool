package nl.grauw.gaia_tool;

import java.io.File;
import java.io.IOException;

public class FilePatch extends Patch {
	
	private File source;
	
	public FilePatch(File source) {
		if (!source.getName().endsWith(".gaia"))
			throw new RuntimeException("Not a GAIA file");
		
		this.source = source;
	}
	
	public File getSource() {
		return source;
	}
	
	public String getName() {
		return source.getName().replaceFirst("\\.[^.]+$", "");
	}
	
	public void load() throws IOException {
		new PatchLoader(this).load(source);
	}
	
}
