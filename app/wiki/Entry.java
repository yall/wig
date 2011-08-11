package wiki;

import java.io.File;

public abstract class Entry {
	public String path;
	public String name;
	Entry(String path) {
		this.path = path.startsWith("/") ? path.substring(1) : path;
		this.name = new File(path).getName();
	}
}
