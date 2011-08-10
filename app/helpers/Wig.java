package helpers;

import play.Play;

public class Wig {
	
	public static final String REPOSITORY_DIR = Play.configuration.getProperty("wig.repository.dir");
	public static final String MARKUP_FILE_EXTENSION = Play.configuration.getProperty("wig.markup.extension");
	
}
