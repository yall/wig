package controllers;

import java.util.List;

import play.Logger;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import wiki.Article;
import wiki.Category;
import wiki.Engine;
import wiki.Entry;
import wiki.Storage;

public class Wiki extends Controller {

	@Before
	public static void log() {
		Logger.info("action : " + request.action);
		Logger.info("path : " + request.path);
	}
	
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
		
		// Enable edition on current version
		article.editable = true;
		
		render(article, entries, html);
	}
	
	public static void list(String path) {	
		Category category = new Category(path);
		
		// Retrieve sub entries if path is a category
		List<Entry> entries = 
			Storage.s().list(path);
		
		render(category, entries);
	}
	
	public static void version(String path, String version) {
		Article article = Engine.retrieve(path, version);
		article.version.version = version;
		String html = Engine.html(article);
		
		// Disable edition on past version
		article.editable = false;
		
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
	
	//TODO assure path = article.path
	public static void save(String path, Article article) {
		article.path = path;
		Engine.save(article);
		flash.success("Article \"%s\" has been successfully saved.", article.path);
		show(article.path);
	}
	
}
