package wiki;

import helpers.Git;
import helpers.Wig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import wiki.Article.Version;

public abstract class Storage {

	enum Policy {
		FS, GIT;
	}
	
	private Storage() {
		
	}
	
	private static final Storage INSTANCE;
	static {
		if ("FS".equals(Wig.STORAGE_POLICY)) {
			INSTANCE = new FSStorage();
		} else {
			INSTANCE = new GitStorage();
		}
	}
	
	public static Storage s() {
		return INSTANCE;
	}
	
	public abstract Article retrieve(String path);
	public abstract Article retrieve(String path, String version);
	public abstract Article save(Article article);
	public abstract List<Article.Version> history(Article article);
	public abstract List<Entry> list(String path);
	public abstract Version currentVersion(Article article);
	
	static class FSStorage extends Storage {

		protected File fileFor(Article article) {
			return fileFor(article.path);
		}
		protected File fileFor(String path) {
			return new File(Wig.STORAGE_DIR, path + Wig.MARKUP_FILE_EXTENSION);
		}
		protected File dirFor(String path) {
			return new File(Wig.STORAGE_DIR, path);
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
					throw new RuntimeException("Unable to write " + article.path, e);
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
				
			if (article.exists && article.version == null) {
				article.version = 
					new Article.Version("current", "anonymous", "", new Date());
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
				article.version = currentVersion(article);
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
			throw new NotImplementedException();
			/*List<Article.Version> versions =
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
			return versions;*/
		}
		
		
		@Override
		public List<Entry> list(String path) {
			List<Entry> entries = new ArrayList<Entry>();
			
			File dir = dirFor(path);
			if (dir.exists() && dir.isDirectory()) {
				String[] entriesNames = dir.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return (!name.startsWith("."));
					}
				});
				for (String entryName : entriesNames) {
					File child = new File(dir, entryName);
					if (child.isDirectory()) {
						String categoryPath = 
							new File(path, entryName)
							.getPath();
						entries.add(new Category(categoryPath));
					} else {
						String articlePath =
							new File(
								path,
								entryName.substring(0, entryName.indexOf(Wig.MARKUP_FILE_EXTENSION)))
							.getPath();
						entries.add(retrieve(articlePath));
					}
				}
			}
			
			return entries;
		}
		
		@Override
		public Version currentVersion(Article article) {
			return 
				new Version("-", "-", "-", new Date(fileFor(article).lastModified()));
		}
		
		
	}

	static class GitStorage extends FSStorage {
		@Override
		public Article save(Article article) {
			article.version.date = new Date();
			Article r = super.save(article);
			Git.repo().username(article.version.user);
			Git.repo().add(".");
			Git.repo().commit(article.version.comment);		
			return r;
		}
		
		@Override
		public List<Version> history(Article article) {
			File file = fileFor(article);
			List<String> logs =
				Git.repo().log(file.getPath());
			return toVersions(logs);
		}
		
		@Override
		public Version currentVersion(Article article) {
			File file = fileFor(article);
			List<String> logs =
				Git.repo().log(file.getPath(), 1);
			return toVersion(logs.get(0));
		}
		
		private List<Version> toVersions(List<String> logs) {
			List<Version> versions = new ArrayList<Article.Version>();
			for (String log : logs) {
				versions.add(toVersion(log));
			}
			return versions;
		}
		
		private Version toVersion(String line) {
			String[] parts = line.split("\\t");
			String hash = parts[0];
			Date date = new Date(Long.valueOf(parts[1]).longValue() * 1000);
			String user = parts[2];
			String comment = parts[3];
			if (comment != null) {
				if (comment.startsWith("\"")) {
					comment = comment.substring(1);
				}
				if (comment.endsWith("\"")) {
					comment = comment.substring(0, comment.length() - 1);
				}
			}
			return new Version(hash, user, comment, date);
		}
	}
}
