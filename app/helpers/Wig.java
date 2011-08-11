package helpers;

import play.Play;

public class Wig {
	
	public static final String STORAGE_DIR = 
		Play.configuration.getProperty("wig.storage.dir");
	
	public static final String STORAGE_POLICY =
		Play.configuration.getProperty("wig.storage.policy");
	
	public static final String MARKUP_FILE_EXTENSION = 
		Play.configuration.getProperty("wig.markup.extension");
	
	
	public static void validate() {
		
	}
}
