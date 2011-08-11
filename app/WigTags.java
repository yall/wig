import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.Map;

import com.sun.javadoc.Type;

import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import wiki.Category;
import wiki.Entry;


@FastTags.Namespace("wig") 
public class WigTags extends FastTags {

	
	public static void _a(Map<?, ?> args, Closure body, PrintWriter out, 
			   ExecutableTemplate template, int fromLine) {
		
		Entry entry = (Entry) args.get("entry");
		
		if (entry.getClass().equals(Category.class)) {
			out.write(entry.name + "/");
			
		} else {
			out.write(entry.name + " (a)");
		}
		
		
		
	}
	
}
