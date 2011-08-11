package wiki;

import java.util.Date;

import play.data.validation.Match;
import play.db.jpa.Model;

public class Article extends Entry {
	
	@Match("([a-z]+/{1})+([a-z]+)")
	public String path;
	
	public String content;
	public boolean exists = false;
	public Version version = new Version();
	public boolean editable = false;
	
	public Article(String path) {
		super(path);
		this.leaf = true;
	}
	
	public Article(String path, String content) {
		super(path);
		this.content = content;
	}
	
	public static class Version {
		public String version;
		public String user;
		public String comment;
		public Date date;
		public Version() {
			
		}
		public Version(String version, String user, String comment, Date date) {
			this.version = version;
			this.user = user;
			this.comment = comment;
			this.date = date;
		}
	}
	
}
