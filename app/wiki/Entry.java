package wiki;

import java.io.File;

import play.data.validation.Match;

public abstract class Entry {
	
	public Category parent;
	
	public String path;
	public String name;
	public boolean leaf;
	Entry(String path) {
		this.path = path.startsWith("/") ? path.substring(1) : path;
		this.name = new File(path).getName();
		File file = new File(path);
		this.parent = 
			(file.getParentFile() != null) ? 
					new Category(file.getParentFile().getPath()) :
					null;
	}
}
