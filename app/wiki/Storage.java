package wiki;

import helpers.Wig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import wiki.Article.Version;

public abstract class Storage {

	enum Policy {
		FS, GIT;
	}
	
	public static Storage mode(Policy mode) {
		return new FSStorage();
	}
	
	public abstract Article retrieve(String path);
	public abstract Article retrieve(String path, String version);
	public abstract Article save(Article article);
	public abstract List<Article.Version> history(Article article);

	
	static class FSStorage extends Storage {

		private File fileFor(Article article) {
			return fileFor(article.path);
		}
		private File fileFor(String path) {
			return new File(Wig.REPOSITORY_DIR, path + Wig.MARKUP_FILE_EXTENSION);
		}
		
		@Override
		public Article save(Article article) {
			File file = fileFor(article);
			
			// Assure file existence
			if (!file.exists()) {
				File parent = file.getParentFile();
				if (parent.exists() || parent.mkdirs()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						throw new RuntimeException("Unable to save article " 
								+ article.path, e);
					}
				}
			}
			
			// Write content
			if (article.content != null) {
				FileWriter writer = null;
				try {
					writer = new FileWriter(file);
					writer.write(article.content, 0, article.content.length());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (writer != null) {
						try { 
							writer.close(); 
						} catch (IOException e) {
							// hmmm silence...
						}
					}
				}
				
			}
			
			article.exists = file.exists();
			
			if (article.exists) {
				article.version = 
					new Article.Version("current", "me", "last commit", new Date());
			}
			
			return article;
		}

		@Override
		public Article retrieve(String path) {
			Article article = new Article(path);
			File file = fileFor(path);
			// If exists, retrieve content;
			if (file.exists()) {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(file));
					StringBuffer content = new StringBuffer();
					String line;
					while ((line = reader.readLine()) != null) {
						content.append(line);
						content.append('\n');
					}
					article.content = content.toString();
					article.exists = true;
				} catch (FileNotFoundException e) {
					throw new RuntimeException("Unable to retrieve article " 
							+ article.path, e);
				} catch (IOException e) {
					throw new RuntimeException("Unable to retrieve article " 
							+ article.path, e);
				}
			}
				
			if (article.exists) {
				article.version = 
					new Article.Version("current", "me", "last commit", new Date());
			}
			
			return article;
		}
		
		@Override
		public Article retrieve(String path, String version) {
			Article article = retrieve(path);
			article.version = new Article.Version(version, "", "", new Date());
			return article;
		}
		
		@Override
		public List<Version> history(Article article) {
			List<Article.Version> versions =
				new ArrayList<Article.Version>();
			
			versions.add(new Article.Version("001", "yall", "First edition", new Date("12/11/1983")));
			versions.add(new Article.Version("002", "yall", "Second edition", new Date("12/11/2010")));
			versions.add(new Article.Version("003", "yall", "Third edition", new Date("08/08/2011")));
			
			Collections.sort(versions, new Comparator<Article.Version>() {
				@Override
				public int compare(Article.Version m1, Article.Version m2) {
					return m2.date.compareTo(m1.date);
				}
				
			});
			return versions;
		}
		
	}
}
