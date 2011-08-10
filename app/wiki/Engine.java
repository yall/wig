package wiki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import wiki.Storage.Policy;


public class Engine {
	
	public static Article retrieve(String path) {
		return Storage.mode(Policy.FS).retrieve(path);
	}
	
	public static Article retrieve(String path, String version) {
		return Storage.mode(Policy.FS).retrieve(path, version);
	}
	
	public static Article save(Article article) {
		return Storage.mode(Policy.FS).save(article);
	}
	
	public static String html(Article article) {
		if (article.content != null) {
			String html = 
				new jj.play.org.eclipse.mylyn.wikitext.core.parser.MarkupParser(
						new jj.play.org.eclipse.mylyn.wikitext.textile.core.TextileLanguage()
				).parseToHtml(article.content);
			html = html.substring(html.indexOf("<body>") + 6, html.lastIndexOf("</body>"));
			return html;
		} else {
			return "";
		}
		
	}
	
	/**
	 * Returns a list of modifications for the article
	 * Empty list if article does not exist yet
	 */
	public static List<Article.Version> history(Article article) {
		return Storage.mode(Policy.FS).history(article);
	}
	
}
