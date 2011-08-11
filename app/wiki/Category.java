package wiki;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.db.jpa.Model;

public class Category extends Entry {

	public String path;
	
	public Category(String path) {
		// Assure the final slash
		super(path.endsWith("/") ? path : path + "/");
	}
	
	/**
	 * Split into each category of the path
	 * "a/b/c".split() -> ["a/","a/b/","a/b/c"]
	 */
	public List<Category> split() {
		System.out.println("split " + this.path);
		List<Category> list = new ArrayList<Category>();
		if (this.parent != null) {
			list.addAll(this.parent.split());
		} 
		list.add(this);
		return list;
	}
	
	
}
