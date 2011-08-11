package jobs;

import helpers.Wig;
import play.Logger;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Bootstrap extends Job {

	 public void doJob() {
		Logger.info("Validate Wig configuration ...");
		
		Logger.info("... storage dir : " + Wig.STORAGE_DIR);
		// Vérifier existense + droits ecriture
		
		Logger.info("... storage policy: " + Wig.STORAGE_POLICY);
		// Verfier que git est la sinon -> fs
		
		Logger.info("... markup file extension : " + Wig.MARKUP_FILE_EXTENSION);
		// Verifier que c'est pas .lock ou un truc qu'on veut garder
    }
}
