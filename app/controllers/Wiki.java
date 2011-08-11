package controllers;

import java.util.Date;
import java.util.List;

import play.mvc.Controller;
import wiki.Article;
import wiki.Engine;
import wiki.Entry;
import wiki.Storage;

public class Wiki extends Controller {

	public static void index() {
		List<Entry> entries = Storage.s().list("");
		render(entries);
	}
	
	public static void show(String path) {
		// Retrieve article
		Article article = Engine.retrieve(path);
		String html = Engine.html(article);
		
		// Retrieve sub entries if path is a category
		List<Entry> entries = Storage.s().list(path);
		
		render(article, entries, html);
	}
	
	public static void version(String path, String version) {
		Article article = Engine.retrieve(path, version);
		article.version.version = version;
		String html = Engine.html(article);
		renderTemplate("Wiki/show.html", article, html);
	}
	
	public static void history(String path) {
		Article article = Engine.retrieve(path);
		if (!article.exists) {
			edit(path);
		}
		List<Article.Version> history = 
			Engine.history(article);
		render(article, history);
	}
	
	public static void edit(String path) {
		Article article = Engine.retrieve(path);
		render(article);
	}
	
	public static void save(String path, Article article) {
		article.path = path;
		Engine.save(article);
		System.out.println(article.path);
		show(article.path);
	}
	
}
