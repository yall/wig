package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import helpers.Wig;
import play.mvc.Controller;
import wiki.Article;
import wiki.Engine;

public class Wiki extends Controller {

	public static void index() {
		render();
	}
	
	public static void show(String path) {
		Article article = Engine.retrieve(path);
		String html = Engine.html(article);
		render(article, html);
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
