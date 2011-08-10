package wiki;

import java.util.Date;

import play.db.jpa.Model;

public class Article {

	public String path;
	public String content;
	public boolean exists = false;
	public Version version;
	
	public Article(String path) {
		this.path = path;
	}
	
	public Article(String path, String content) {
		this.path = path;
		this.content = content;
	}
	
	public static Article findByPath(String path) {
		return new Article(path, "Lorem ipsum...");
	}
	
	public static class Version {
		public String version;
		public String user;
		public String comment;
		public Date date;
		public Version(String version, String user, String comment, Date date) {
			this.version = version;
			this.user = user;
			this.comment = comment;
			this.date = date;
		}
	}
	
}
